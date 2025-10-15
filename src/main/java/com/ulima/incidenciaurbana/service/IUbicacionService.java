package com.ulima.incidenciaurbana.service;

import com.ulima.incidenciaurbana.dto.UbicacionDTO;

public interface IUbicacionService {
    UbicacionDTO obtenerUbicacionPorId(Long id);
    UbicacionDTO actualizarUbicacion(Long id, UbicacionDTO ubicacionDTO);
}
