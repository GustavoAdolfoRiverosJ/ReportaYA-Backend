package com.ulima.incidenciaurbana.service;

import com.ulima.incidenciaurbana.dto.TecnicoDTO;
import org.springframework.data.domain.Page;

public interface ITecnicoService {
    /**
     * Devuelve todos los técnicos paginados (10 por página)
     * @param page número de página (0-based)
     */
    Page<TecnicoDTO> obtenerTodosTecnicos(int page);
}
