package com.ulima.incidenciaurbana.service;

import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Prioridad;
import java.util.List;

public interface IReporteService {
    ReporteDTO crearReporte(ReporteDTO reporteDTO);

    ReporteDTO obtenerReportePorId(Long id);

    List<ReporteDTO> obtenerTodosReportes();

    List<ReporteDTO> obtenerReportesPorEstado(EstadoReporte estado);

    List<ReporteDTO> obtenerReportesPorCuenta(Long cuentaId);

    ReporteDTO actualizarReporte(Long id, ReporteDTO reporteDTO);

    ReporteDTO cambiarEstadoReporte(Long id, EstadoReporte nuevoEstado);

    ReporteDTO cambiarPrioridadReporte(Long id, Prioridad nuevaPrioridad);

    void eliminarReporte(Long id);

    ReporteDTO asignarTecnicoReporte(Long reporteId, Long operadorId, Long tecnicoId);
}
