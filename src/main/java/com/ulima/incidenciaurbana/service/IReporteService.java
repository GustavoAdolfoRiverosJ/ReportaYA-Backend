package com.ulima.incidenciaurbana.service;

import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Prioridad;
import java.util.List;
import org.springframework.data.domain.Page;

public interface IReporteService {
    ReporteDTO crearReporte(ReporteDTO reporteDTO);

    Page<ReporteDTO> obtenerTodosReportes(int page);

    // OperadorMunicipal will use obtenerTodosReportes(int page) for paginated access

    org.springframework.data.domain.Page<ReporteDTO> obtenerReportesPorCuenta(Long cuentaId, int page);

    ReporteDTO actualizarReporte(Long id, ReporteDTO reporteDTO);

    ReporteDTO cambiarEstadoReporte(Long id, EstadoReporte nuevoEstado);

    ReporteDTO cambiarPrioridadReporte(Long id, Prioridad nuevaPrioridad);

    void eliminarReporte(Long id);
}
