package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.CrearUsuarioMunicipalDTO;
import com.ulima.incidenciaurbana.dto.UsuarioMunicipalResponseDTO;
import com.ulima.incidenciaurbana.factory.CuentaFactory;
import com.ulima.incidenciaurbana.factory.CuentaFactoryProvider;
import com.ulima.incidenciaurbana.model.*;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.repository.PersonaRepository;
import com.ulima.incidenciaurbana.service.IUsuarioMunicipalService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioMunicipalServiceImpl implements IUsuarioMunicipalService {

    private final CuentaRepository cuentaRepository;
    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    private final CuentaFactoryProvider cuentaFactoryProvider;


public UsuarioMunicipalServiceImpl(CuentaRepository cuentaRepository,
                                   PersonaRepository personaRepository,
                                   PasswordEncoder passwordEncoder,
                                   CuentaFactoryProvider cuentaFactoryProvider) {
    this.cuentaRepository = cuentaRepository;
    this.personaRepository = personaRepository;
    this.passwordEncoder = passwordEncoder;
    this.cuentaFactoryProvider = cuentaFactoryProvider;
}

    @Override
    public UsuarioMunicipalResponseDTO crearUsuarioMunicipal(CrearUsuarioMunicipalDTO dto) {

        // 1. Validaciones igual que ya tienes
        if (cuentaRepository.existsByUsuario(dto.getUsuario())) {
            throw new IllegalArgumentException("El usuario ya está registrado");
        }

        if (personaRepository.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // 2. Crear Persona
        Persona persona = new Persona(
                dto.getNombres(),
                dto.getApellidos(),
                dto.getDni(),
                dto.getTelefono(),
                dto.getCorreo()
        );

        // 3. Encriptar password
        String passwordHash = passwordEncoder.encode(dto.getPassword());

        // 4. Obtener factory según rol municipal
        RolMunicipal rol = dto.getRolMunicipal();
        if (rol == null) {
            throw new IllegalArgumentException("Debe especificarse un rol municipal");
        }

        CuentaFactory factory = cuentaFactoryProvider.getFactory(rol);

        // 5. Crear cuenta usando la factory
        Cuenta cuenta = factory.crearCuenta(dto.getUsuario(), passwordHash, persona);

        // Por seguridad, aseguramos el rol (aunque el modelo ya debería setearlo)
        cuenta.setRolMunicipal(rol);

        // 6. Guardar
        Cuenta guardada = cuentaRepository.save(cuenta);

        // 7. Mapear a DTO de respuesta
        return mapToResponseDTO(guardada);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UsuarioMunicipalResponseDTO> listarUsuariosMunicipales() {
        return cuentaRepository.findByRolMunicipalIsNotNull()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private UsuarioMunicipalResponseDTO mapToResponseDTO(Cuenta cuenta) {
        UsuarioMunicipalResponseDTO dto = new UsuarioMunicipalResponseDTO();
        dto.setId(cuenta.getId());
        dto.setUsuario(cuenta.getUsuario());
        dto.setActivo(cuenta.isActivo());
        dto.setRolMunicipal(cuenta.getRolMunicipal());

        if (cuenta.getPersona() != null) {
            dto.setNombres(cuenta.getPersona().getNombres());
            dto.setApellidos(cuenta.getPersona().getApellidos());
            dto.setCorreo(cuenta.getPersona().getCorreo());
        }

        return dto;
    }
}