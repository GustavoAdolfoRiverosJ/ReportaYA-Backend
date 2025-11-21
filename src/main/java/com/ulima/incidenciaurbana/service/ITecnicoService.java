package com.ulima.incidenciaurbana.service;

import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.dto.CompletarReporteRequest;
import org.springframework.data.domain.Page;

public interface ITecnicoService {

    /**
     * Lista reportes asignados al técnico
     * 
     * @param tecnicoId ID del técnico
     * @param estado    Estado para filtrar (PROCESO, RESUELTA) - opcional
     * @param page      Número de página
     * @return Reportes asignados al técnico
     */
    Page<ReporteDTO> obtenerReportesAsignados(Long tecnicoId, String estado, int page);
}
 

