package com.ulima.incidenciaurbana.service;

import com.ulima.incidenciaurbana.dto.CiudadanoDTO;
import java.util.List;

public interface ICiudadanoService {
    CiudadanoDTO crearCiudadano(CiudadanoDTO ciudadanoDTO);
    CiudadanoDTO obtenerCiudadanoPorId(Long id);
    List<CiudadanoDTO> obtenerTodosCiudadanos();
    CiudadanoDTO obtenerCiudadanoPorDni(String dni);
    CiudadanoDTO actualizarCiudadano(Long id, CiudadanoDTO ciudadanoDTO);
    void eliminarCiudadano(Long id);
}
