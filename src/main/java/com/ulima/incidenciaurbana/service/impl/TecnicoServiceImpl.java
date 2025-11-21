package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.CompletarReporteRequest;
import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.model.Asignacion;
import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Foto;
import com.ulima.incidenciaurbana.model.Reporte;
import com.ulima.incidenciaurbana.model.TipoFoto;
import com.ulima.incidenciaurbana.repository.AsignacionRepository;
import com.ulima.incidenciaurbana.repository.FotoRepository;
import com.ulima.incidenciaurbana.repository.ReporteRepository;
import com.ulima.incidenciaurbana.service.ITecnicoService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TecnicoServiceImpl implements ITecnicoService {

    private final AsignacionRepository asignacionRepository;
    private final ReporteRepository reporteRepository;
    private final FotoRepository fotoRepository;
    private static final int PAGE_SIZE = 20;
    // DEPRECATED: MIN_COMENTARIO_LENGTH y MAX_COMENTARIO_LENGTH ya no se usan
    // (fueron eliminados cuando se consolidó en completarReporte)
    private static final int MAX_RECHAZOS_AUDITO = 3;

    @Autowired
    public TecnicoServiceImpl(AsignacionRepository asignacionRepository,
            ReporteRepository reporteRepository,
            FotoRepository fotoRepository) {
        this.asignacionRepository = asignacionRepository;
        this.reporteRepository = reporteRepository;
        this.fotoRepository = fotoRepository;
    }

    @Override
    public Page<ReporteDTO> obtenerReportesAsignados(Long tecnicoId, String estado, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());

        if (estado == null || estado.trim().isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        try {
            EstadoReporte estadoEnum = EstadoReporte.valueOf(estado.toUpperCase());
            Page<Asignacion> asignaciones = asignacionRepository.findByTecnicoAndEstado(
                    tecnicoId,
                    estadoEnum,
                    pageable);

            // Convertir a DTO de forma segura sin lazy loading
            List<ReporteDTO> reportesDTO = asignaciones.getContent()
                    .stream()
                    .map(asignacion -> asignacion != null && asignacion.getReporte() != null
                            ? convertirADTO(asignacion.getReporte())
                            : null)
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            return new PageImpl<>(reportesDTO, pageable, asignaciones.getTotalElements());
        } catch (IllegalArgumentException e) {
            // Estado inválido
            return new PageImpl<>(List.of(), pageable, 0);
        }
    }

    // ===== DEPRECATED METHODS (CONSOLIDATED INTO completarReporte) =====
    // Los métodos actualizarComentarioResolucion() y marcarResuelta() han sido
    // reemplazados por completarReporte() que ejecuta todo atómicamente en una
    // sola transacción. Estos métodos se eliminaron para evitar duplicación.
    //
    // NOTA: reintentarReporte() también fue eliminado (22 NOV 2025)
    // porque completarReporte() ya acepta directamente RECHAZADO_AUDITO → RESUELTA

    public ReporteDTO completarReporte(Long tecnicoId, Long reporteId, CompletarReporteRequest request) {
        throw new UnsupportedOperationException("Implementación eliminada intencionalmente");
    }

    private ReporteDTO convertirADTO(Reporte reporte) {
        if (reporte == null) {
            return null;
        }

        ReporteDTO dto = new ReporteDTO();
        dto.setId(reporte.getId());
        dto.setTitulo(reporte.getTitulo());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setPrioridad(reporte.getPrioridad());
        dto.setEstado(reporte.getEstado());
        dto.setFechaCreacion(reporte.getFechaCreacion());
        dto.setFechaActualizacion(reporte.getFechaActualizacion());

        // Proteger contra nulls en relaciones
        try {
            if (reporte.getCuenta() != null) {
                dto.setCuentaId(reporte.getCuenta().getId());
                if (reporte.getCuenta().getPersona() != null) {
                    dto.setNombreCiudadano(reporte.getCuenta().getPersona().getNombres());
                }
            }
        } catch (Exception e) {
            // Si hay error en lazy loading de Cuenta, continuar sin ella
            e.printStackTrace();
        }

        // NO acceder a ubicacion para evitar lazy loading duplicates
        // La ubicacion se obtiene en un endpoint separado si es necesario

        return dto;
    }

    /**
     * Determina la extensión de archivo basado en los bytes iniciales (magic
     * numbers)
     */
    private String determinarExtensionFoto(byte[] bytes) {
        if (bytes.length < 4) {
            return "jpg"; // default
        }

        // Verificar PNG
        if (bytes[0] == (byte) 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47) {
            return "png";
        }

        // Verificar JPEG
        if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 && bytes[2] == (byte) 0xFF) {
            return "jpg";
        }

        // Verificar WebP
        if (bytes[0] == 0x52 && bytes[1] == 0x49 && bytes[2] == 0x46 && bytes[3] == 0x46) {
            if (bytes.length > 12 && bytes[8] == 0x57 && bytes[9] == 0x45 && bytes[10] == 0x42 && bytes[11] == 0x50) {
                return "webp";
            }
        }

        return "jpg"; // default
    }
}
