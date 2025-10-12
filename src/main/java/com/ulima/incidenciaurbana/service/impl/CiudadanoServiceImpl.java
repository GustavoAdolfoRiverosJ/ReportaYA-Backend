package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.CiudadanoDTO;
import com.ulima.incidenciaurbana.model.Ciudadano;
import com.ulima.incidenciaurbana.model.Persona;
import com.ulima.incidenciaurbana.repository.CiudadanoRepository;
import com.ulima.incidenciaurbana.service.ICiudadanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CiudadanoServiceImpl implements ICiudadanoService {
    
    private final CiudadanoRepository ciudadanoRepository;
    
    @Autowired
    public CiudadanoServiceImpl(CiudadanoRepository ciudadanoRepository) {
        this.ciudadanoRepository = ciudadanoRepository;
    }
    
    @Override
    public CiudadanoDTO crearCiudadano(CiudadanoDTO ciudadanoDTO) {
        Persona persona = new Persona(
            ciudadanoDTO.getNombres(),
            ciudadanoDTO.getApellidos(),
            ciudadanoDTO.getDni(),
            ciudadanoDTO.getTelefono(),
            ciudadanoDTO.getCorreo()
        );
        
        Ciudadano ciudadano = new Ciudadano(
            ciudadanoDTO.getUsuario(),
            "hash_password_placeholder", // En producción, usar BCrypt
            persona
        );
        
        ciudadano = ciudadanoRepository.save(ciudadano);
        return convertirADTO(ciudadano);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CiudadanoDTO obtenerCiudadanoPorId(Long id) {
        Ciudadano ciudadano = ciudadanoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado con id: " + id));
        return convertirADTO(ciudadano);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CiudadanoDTO> obtenerTodosCiudadanos() {
        return ciudadanoRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CiudadanoDTO obtenerCiudadanoPorDni(String dni) {
        Ciudadano ciudadano = ciudadanoRepository.findByPersonaDni(dni)
            .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado con DNI: " + dni));
        return convertirADTO(ciudadano);
    }
    
    @Override
    public CiudadanoDTO actualizarCiudadano(Long id, CiudadanoDTO ciudadanoDTO) {
        Ciudadano ciudadano = ciudadanoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado con id: " + id));
        
        ciudadano.getPersona().setTelefono(ciudadanoDTO.getTelefono());
        ciudadano.getPersona().setCorreo(ciudadanoDTO.getCorreo());
        
        ciudadano = ciudadanoRepository.save(ciudadano);
        return convertirADTO(ciudadano);
    }
    
    @Override
    public void eliminarCiudadano(Long id) {
        if (!ciudadanoRepository.existsById(id)) {
            throw new RuntimeException("Ciudadano no encontrado con id: " + id);
        }
        ciudadanoRepository.deleteById(id);
    }
    
    private CiudadanoDTO convertirADTO(Ciudadano ciudadano) {
        return new CiudadanoDTO(
            ciudadano.getId(),
            ciudadano.getUsuario(),
            ciudadano.getPersona().getNombres(),
            ciudadano.getPersona().getApellidos(),
            ciudadano.getPersona().getDni(),
            ciudadano.getPersona().getTelefono(),
            ciudadano.getPersona().getCorreo(),
            ciudadano.isActivo()
        );
    }
}
