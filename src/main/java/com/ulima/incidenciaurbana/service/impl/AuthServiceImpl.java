package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.LoginRequest;
import com.ulima.incidenciaurbana.dto.LoginResponse;
import com.ulima.incidenciaurbana.exception.InvalidCredentialsException;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.repository.CuentaRepo;
import com.ulima.incidenciaurbana.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final CuentaRepo cuentaRepo;

    public AuthServiceImpl(CuentaRepo cuentaRepo) {
        this.cuentaRepo = cuentaRepo;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Cuenta c = cuentaRepo.findByUsuarioAndActivoTrue(request.getUsuario())
                .orElseThrow(InvalidCredentialsException::new);

        if (!c.getContrasena().equals(request.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String nombre = (c.getPersona() != null) ? c.getPersona().getNombreCompleto() : "";
        return new LoginResponse(c.getId(), c.getUsuario(), nombre, "Login exitoso");
    }
}
