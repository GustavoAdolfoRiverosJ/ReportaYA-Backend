package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.AsignacionDTO;
import com.ulima.incidenciaurbana.model.Asignacion;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.OperadorMunicipal;
import com.ulima.incidenciaurbana.model.Reporte;
import com.ulima.incidenciaurbana.model.Tecnico;
import com.ulima.incidenciaurbana.repository.AsignacionRepository;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.repository.ReporteRepository;
import com.ulima.incidenciaurbana.service.IAsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AsignacionServiceImpl implements IAsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final ReporteRepository reporteRepository;
    private final CuentaRepository cuentaRepository;

    @Autowired
    public AsignacionServiceImpl(AsignacionRepository asignacionRepository,
                                 ReporteRepository reporteRepository,
                                 CuentaRepository cuentaRepository) {
        this.asignacionRepository = asignacionRepository;
        this.reporteRepository = reporteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public AsignacionDTO crearAsignacion(AsignacionDTO asignacionDTO) {
        // Validar reporte
        if (asignacionDTO.getReporteId() == null) {
            throw new RuntimeException("El ID del reporte es obligatorio");
        }
        long reporteId = asignacionDTO.getReporteId();
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new RuntimeException(
                        "Reporte no encontrado con id: " + reporteId));

        // Validar que el reporte esté en estado REVISION
        if (reporte.getEstado() != EstadoReporte.REVISION) {
            throw new RuntimeException(
                    "El reporte debe estar en estado REVISION para poder asignar un técnico. Estado actual: " + reporte.getEstado());
        }

        // Validar y obtener operador municipal
        if (asignacionDTO.getOperadorId() == null) {
            throw new RuntimeException("El ID del operador es obligatorio");
        }
        long operadorId = asignacionDTO.getOperadorId();
        Cuenta operadorCuenta = cuentaRepository.findById(operadorId)
                .orElseThrow(() -> new RuntimeException(
                        "Operador no encontrado con id: " + operadorId));

        if (!(operadorCuenta instanceof OperadorMunicipal)) {
            throw new RuntimeException(
                    "El usuario con id " + operadorId + " no es un operador municipal");
        }

        OperadorMunicipal operador = (OperadorMunicipal) operadorCuenta;

        // Validar y obtener técnico
        if (asignacionDTO.getTecnicoId() == null) {
            throw new RuntimeException("El ID del técnico es obligatorio");
        }
        long tecnicoId = asignacionDTO.getTecnicoId();
        Cuenta tecnicoCuenta = cuentaRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException(
                        "Técnico no encontrado con id: " + tecnicoId));

        if (!(tecnicoCuenta instanceof Tecnico)) {
            throw new RuntimeException(
                    "El usuario con id " + tecnicoId + " no es un técnico");
        }

        Tecnico tecnico = (Tecnico) tecnicoCuenta;

        // Cerrar asignación previa si existe (solo puede haber una asignación activa por reporte)
        asignacionRepository.findByReporteIdAndFechaCierreIsNull(reporteId)
                .ifPresent(asignacionPrevia -> {
                    asignacionPrevia.setFechaCierre(java.time.LocalDateTime.now());
                    asignacionRepository.save(asignacionPrevia);
                });

        // Crear la nueva asignación usando el modelo
        Asignacion asignacion = operador.asignarTecnico(reporte, tecnico);
        if (asignacion == null) {
            throw new RuntimeException("Error al crear la asignación");
        }
        asignacion = asignacionRepository.save(asignacion);

        // Actualizar estado del reporte a PROCESO
        reporte.cambiarEstado(EstadoReporte.PROCESO);
        reporte.agregarAsignacion(asignacion);
        reporteRepository.save(reporte);

        return convertirADTO(asignacion);
    }

    /**
     * Convierte una entidad Asignacion a AsignacionDTO
     */
    private AsignacionDTO convertirADTO(Asignacion asignacion) {
        return new AsignacionDTO(
                asignacion.getId(),
                asignacion.getReporte().getId(),
                asignacion.getOperador().getId(),
                asignacion.getTecnico().getId(),
                asignacion.getReporte().getTitulo(),
                asignacion.getOperador().getPersona().getNombreCompleto(),
                asignacion.getTecnico().getPersona().getNombreCompleto(),
                asignacion.getFechaAsignacion(),
                asignacion.getFechaCierre()
        );
    }
}
