package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.LoginRequest;
import com.ulima.incidenciaurbana.dto.LoginResponse;
import com.ulima.incidenciaurbana.exception.InvalidCredentialsException;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final CuentaRepository cuentaRepository;

    public AuthServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
    Cuenta c = cuentaRepository.findByUsuarioAndActivoTrue(request.getUsuario())
        .orElseThrow(InvalidCredentialsException::new);

        if (!c.getContrasenaHash().equals(request.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String nombre = (c.getPersona() != null) ? c.getPersona().getNombreCompleto() : "";
        return new LoginResponse(c.getId(), c.getUsuario(), nombre, "Login exitoso");
    }
}
