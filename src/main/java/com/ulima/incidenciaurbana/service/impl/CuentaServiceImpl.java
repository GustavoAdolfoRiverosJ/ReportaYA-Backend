package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.CuentaDTO;
import com.ulima.incidenciaurbana.factory.CuentaFactory;
import com.ulima.incidenciaurbana.factory.CuentaFactoryProvider;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.Persona;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.service.ICuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio unificado de cuentas
 * Utiliza el patrón Abstract Factory para crear diferentes tipos de cuentas
 */
@Service
@Transactional
public class CuentaServiceImpl implements ICuentaService {
    
    private final CuentaFactoryProvider factoryProvider;
    private final CuentaRepository cuentaRepository;
    
    @Autowired
    public CuentaServiceImpl(CuentaFactoryProvider factoryProvider, CuentaRepository cuentaRepository) {
        this.factoryProvider = factoryProvider;
        this.cuentaRepository = cuentaRepository;
    }
    
    @Override
    public CuentaDTO crearCuenta(CuentaDTO dto) {
        // Validar tipo de cuenta
        if (!factoryProvider.esValido(dto.getTipoCuenta())) {
            throw new IllegalArgumentException(
                "Tipo de cuenta no válido: " + dto.getTipoCuenta() + 
                ". Tipos válidos: CIUDADANO, TECNICO, OPERADOR_MUNICIPAL"
            );
        }
        
        // Crear persona
        Persona persona = new Persona(
            dto.getNombres(),
            dto.getApellidos(),
            dto.getDni(),
            dto.getTelefono(),
            dto.getCorreo()
        );
        
        // Obtener la factory correspondiente
        CuentaFactory factory = factoryProvider.getFactory(dto.getTipoCuenta());
        
        // Crear cuenta usando la factory (Abstract Factory Pattern)
        Cuenta cuenta = factory.crearCuenta(dto.getUsuario(), dto.getContrasena(), persona);
        cuenta.setActivo(dto.getActivo());
        
        // Guardar en base de datos
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        
        // Convertir a DTO de respuesta (mismo DTO, ahora con ID)
        CuentaDTO response = new CuentaDTO();
        response.setId(cuentaGuardada.getId());
        response.setTipoCuenta(dto.getTipoCuenta());
        response.setUsuario(cuentaGuardada.getUsuario());
        response.setNombres(cuentaGuardada.getPersona().getNombres());
        response.setApellidos(cuentaGuardada.getPersona().getApellidos());
        response.setDni(cuentaGuardada.getPersona().getDni());
        response.setTelefono(cuentaGuardada.getPersona().getTelefono());
        response.setCorreo(cuentaGuardada.getPersona().getCorreo());
        response.setActivo(cuentaGuardada.isActivo());
        // No incluimos la contraseña en la respuesta
        
        return response;
    }
}
