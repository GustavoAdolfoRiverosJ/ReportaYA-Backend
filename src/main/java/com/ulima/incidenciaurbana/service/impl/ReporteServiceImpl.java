package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.dto.UbicacionDTO;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Prioridad;
import com.ulima.incidenciaurbana.model.Reporte;
import com.ulima.incidenciaurbana.model.Ubicacion;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.repository.ReporteRepository;
import com.ulima.incidenciaurbana.repository.UbicacionRepository;
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
    private final UbicacionRepository ubicacionRepository;
    
    @Autowired
    public ReporteServiceImpl(ReporteRepository reporteRepository, CuentaRepository cuentaRepository, UbicacionRepository ubicacionRepository) {
        this.reporteRepository = reporteRepository;
        this.cuentaRepository = cuentaRepository;
        this.ubicacionRepository = ubicacionRepository;
    }
    
    @Override
    public ReporteDTO crearReporte(ReporteDTO reporteDTO) {
        // Validar que la ubicación sea obligatoria
        if (reporteDTO.getUbicacion() == null) {
            throw new RuntimeException("La ubicación es obligatoria para crear un reporte");
        }
        
        // Validar que la ubicación tenga latitud y longitud
        if (reporteDTO.getUbicacion().getLatitud() == null || reporteDTO.getUbicacion().getLongitud() == null) {
            throw new RuntimeException("La latitud y longitud son obligatorias en la ubicación");
        }
        
        Cuenta cuenta = cuentaRepository.findById(reporteDTO.getCuentaId())
            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + reporteDTO.getCuentaId()));
        
        Reporte reporte = new Reporte(
            reporteDTO.getTitulo(),
            reporteDTO.getDescripcion(),
            cuenta
        );
        
        if (reporteDTO.getPrioridad() != null) {
            reporte.setPrioridad(reporteDTO.getPrioridad());
        }
        
        // Crear y asociar ubicación (ahora es obligatoria)
        Ubicacion ubicacion = convertirDTOAUbicacion(reporteDTO.getUbicacion());
        ubicacion = ubicacionRepository.save(ubicacion);
        reporte.setUbicacion(ubicacion);
        
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
        
        // La ubicación es obligatoria, debe estar presente
        if (reporteDTO.getUbicacion() == null) {
            throw new RuntimeException("La ubicación es obligatoria. No se puede actualizar un reporte sin ubicación");
        }
        
        // Validar que la ubicación tenga latitud y longitud
        if (reporteDTO.getUbicacion().getLatitud() == null || reporteDTO.getUbicacion().getLongitud() == null) {
            throw new RuntimeException("La latitud y longitud son obligatorias en la ubicación");
        }
        
        // Actualizar ubicación existente
        Ubicacion ubicacion = reporte.getUbicacion();
        ubicacion.setLatitud(reporteDTO.getUbicacion().getLatitud());
        ubicacion.setLongitud(reporteDTO.getUbicacion().getLongitud());
        ubicacion.setDireccion(reporteDTO.getUbicacion().getDireccion());
        ubicacionRepository.save(ubicacion);
        
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
            reporte.getEstado()
        );
        
        dto.setFechaCreacion(reporte.getFechaCreacion());
        dto.setFechaActualizacion(reporte.getFechaActualizacion());
        
        // Convertir ubicación si existe
        if (reporte.getUbicacion() != null) {
            dto.setUbicacion(convertirUbicacionADTO(reporte.getUbicacion()));
        }
        
        return dto;
    }
    
    private UbicacionDTO convertirUbicacionADTO(Ubicacion ubicacion) {
        return new UbicacionDTO(
            ubicacion.getId(),
            ubicacion.getLatitud(),
            ubicacion.getLongitud(),
            ubicacion.getDireccion(),
            ubicacion.getFechaRegistro()
        );
    }
    
    private Ubicacion convertirDTOAUbicacion(UbicacionDTO dto) {
        Ubicacion ubicacion = new Ubicacion(
            dto.getLatitud(),
            dto.getLongitud(),
            dto.getDireccion()
        );
        return ubicacion;
    }
}
