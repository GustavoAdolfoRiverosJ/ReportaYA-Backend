package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.PasswordResetToken;
import com.ulima.incidenciaurbana.model.Persona;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.repository.PasswordResetTokenRepository;
import com.ulima.incidenciaurbana.repository.PersonaRepository;
import com.ulima.incidenciaurbana.service.IEmailService;
import com.ulima.incidenciaurbana.service.IRecuperacionContrasenaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; 
import java.util.Optional;
import java.util.UUID;

@Service
public class RecuperacionContrasenaServiceImpl implements IRecuperacionContrasenaService {

    private final PersonaRepository personaRepository;
    private final CuentaRepository cuentaRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    // Leemos la URL base del front desde application.properties
    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordBaseUrl;

    public RecuperacionContrasenaServiceImpl(PersonaRepository personaRepository,
                                             CuentaRepository cuentaRepository,
                                             PasswordResetTokenRepository tokenRepository,
                                             PasswordEncoder passwordEncoder,
                                             IEmailService emailService) {
        this.personaRepository = personaRepository;
        this.cuentaRepository = cuentaRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void solicitarRecuperacion(String correo) {
        Optional<Persona> personaOpt = personaRepository.findByCorreo(correo);

        // Por seguridad SIEMPRE respondemos ok al front,
        // pero solo hacemos algo si existe.
        if (personaOpt.isEmpty()) {
            return;
        }

        Persona persona = personaOpt.get();

        Optional<Cuenta> cuentaOpt = cuentaRepository.findByPersona(persona);
        if (cuentaOpt.isEmpty()) {
            return;
        }

        Cuenta cuenta = cuentaOpt.get();

        // Generar token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusHours(1);

        PasswordResetToken tokenEntity = new PasswordResetToken(token, cuenta, expiracion);
        tokenRepository.save(tokenEntity);

        // Armar enlace final para el correo (ej: http://localhost:4200/reset-password?token=XYZ)
        String link = resetPasswordBaseUrl + "?token=" + token;

        emailService.enviarCorreoRecuperacionContrasena(persona.getCorreo(), link);
    }

    @Override
    public boolean validarToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken t = tokenOpt.get();

        if (t.isUsado() || t.estaExpirado()) {
            return false;
        }

        return true;
    }

    @Override
    public void cambiarContrasena(String token, String nuevaContrasena) {
        PasswordResetToken tokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (tokenEntity.isUsado() || tokenEntity.estaExpirado()) {
            throw new IllegalStateException("El enlace de recuperación no es válido o ha expirado");
        }

        Cuenta cuenta = tokenEntity.getCuenta();
        String hash = passwordEncoder.encode(nuevaContrasena);
        cuenta.setContrasenaHash(hash);
        cuentaRepository.save(cuenta);

        tokenEntity.setUsado(true);
        tokenRepository.save(tokenEntity);
    }
}
