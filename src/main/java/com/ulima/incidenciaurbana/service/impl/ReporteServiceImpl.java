package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Prioridad;
import com.ulima.incidenciaurbana.model.Reporte;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.repository.AsignacionRepository;
import com.ulima.incidenciaurbana.repository.ReporteRepository;
import com.ulima.incidenciaurbana.service.IReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReporteServiceImpl implements IReporteService {

    private final ReporteRepository reporteRepository;
    private final CuentaRepository cuentaRepository;
    private final AsignacionRepository asignacionRepository;

    @Autowired
    public ReporteServiceImpl(ReporteRepository reporteRepository, CuentaRepository cuentaRepository,
            AsignacionRepository asignacionRepository) {
        this.reporteRepository = reporteRepository;
        this.cuentaRepository = cuentaRepository;
        this.asignacionRepository = asignacionRepository;
    }

    @Override
    public ReporteDTO crearReporte(ReporteDTO reporteDTO) {
        Cuenta cuenta = cuentaRepository.findById(reporteDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + reporteDTO.getCuentaId()));

        Reporte reporte = new Reporte(
                reporteDTO.getTitulo(),
                reporteDTO.getDescripcion(),
                cuenta);

        if (reporteDTO.getPrioridad() != null) {
            reporte.setPrioridad(reporteDTO.getPrioridad());
        }

        cuenta.crearReporte(reporte);
        reporte = reporteRepository.save(reporte);
        return convertirADTO(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteDTO obtenerReportePorId(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        return convertirADTO(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> obtenerTodosReportes() {
        return reporteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> obtenerReportesPorEstado(EstadoReporte estado) {
        return reporteRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> obtenerReportesPorCuenta(Long cuentaId) {
        return reporteRepository.findByCuentaId(cuentaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReporteDTO actualizarReporte(Long id, ReporteDTO reporteDTO) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));

        reporte.setTitulo(reporteDTO.getTitulo());
        reporte.setDescripcion(reporteDTO.getDescripcion());

        if (reporteDTO.getPrioridad() != null) {
            reporte.setPrioridad(reporteDTO.getPrioridad());
        }

        reporte = reporteRepository.save(reporte);
        return convertirADTO(reporte);
    }

    @Override
    public ReporteDTO cambiarEstadoReporte(Long id, EstadoReporte nuevoEstado) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));

        reporte.cambiarEstado(nuevoEstado);
        reporte = reporteRepository.save(reporte);
        return convertirADTO(reporte);
    }

    @Override
    public ReporteDTO cambiarPrioridadReporte(Long id, Prioridad nuevaPrioridad) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));

        reporte.cambiarPrioridad(nuevaPrioridad);
        reporte = reporteRepository.save(reporte);
        return convertirADTO(reporte);
    }

    @Override
    public void eliminarReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
    }

    private ReporteDTO convertirADTO(Reporte reporte) {
        ReporteDTO dto = new ReporteDTO(
                reporte.getId(),
                reporte.getTitulo(),
                reporte.getDescripcion(),
                reporte.getCuenta().getId(),
                reporte.getCuenta().getPersona().getNombreCompleto(),
                reporte.getPrioridad(),
                reporte.getEstado());

        dto.setFechaCreacion(reporte.getFechaCreacion());
        dto.setFechaActualizacion(reporte.getFechaActualizacion());

        return dto;
    }

    @Override
    public ReporteDTO triajeYAsignar(Long reporteId, Long operadorId,
            com.ulima.incidenciaurbana.dto.TriajeRequest triajeRequest) {
        // Validar reporte
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + reporteId));

        // Validar estado APROBADO (mapeado a REVISION en el enum existente)
        if (reporte.getEstado() != com.ulima.incidenciaurbana.model.EstadoReporte.REVISION) {
            throw new RuntimeException("Reporte no está en estado APROBADO (se requiere estado REVISION)");
        }

        // Validar prioridad
        if (triajeRequest.getPrioridad() == null) {
            throw new RuntimeException("Prioridad es obligatoria");
        }

        // Buscar tecnico
        com.ulima.incidenciaurbana.model.Cuenta tecnicoCuenta = cuentaRepository.findById(triajeRequest.getTecnicoId())
                .orElseThrow(
                        () -> new RuntimeException("Tecnico no encontrado con id: " + triajeRequest.getTecnicoId()));

        if (!(tecnicoCuenta instanceof com.ulima.incidenciaurbana.model.Tecnico)) {
            throw new RuntimeException("El usuario especificado no es un técnico");
        }

        com.ulima.incidenciaurbana.model.Tecnico tecnico = (com.ulima.incidenciaurbana.model.Tecnico) tecnicoCuenta;

        // Cerrar asignacion previa si existe
        asignacionRepository.findByReporteIdAndFechaCierreIsNull(reporteId).ifPresent(prev -> {
            prev.setFechaCierre(java.time.LocalDateTime.now());
            asignacionRepository.save(prev);
        });

        // Crear nueva asignacion
        com.ulima.incidenciaurbana.model.OperadorMunicipal operador = null;
        if (operadorId != null) {
            com.ulima.incidenciaurbana.model.Cuenta operadorCuenta = cuentaRepository.findById(operadorId)
                    .orElseThrow(() -> new RuntimeException("Operador no encontrado con id: " + operadorId));
            if (!(operadorCuenta instanceof com.ulima.incidenciaurbana.model.OperadorMunicipal)) {
                throw new RuntimeException("El usuario especificado no es un operador municipal");
            }
            operador = (com.ulima.incidenciaurbana.model.OperadorMunicipal) operadorCuenta;
        }

        com.ulima.incidenciaurbana.model.Asignacion asignacion = new com.ulima.incidenciaurbana.model.Asignacion(
                reporte, operador, tecnico);
        asignacion = asignacionRepository.save(asignacion);

        // Actualizar prioridad y estado
        reporte.setPrioridad(triajeRequest.getPrioridad());
        reporte.setEstado(com.ulima.incidenciaurbana.model.EstadoReporte.PROCESO);
        reporte.agregarAsignacion(asignacion);
        reporte = reporteRepository.save(reporte);

        return convertirADTO(reporte);
    }
}
