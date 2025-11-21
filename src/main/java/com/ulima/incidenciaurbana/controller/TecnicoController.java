package com.ulima.incidenciaurbana.controller;

import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.dto.CompletarReporteRequest;
import com.ulima.incidenciaurbana.service.ITecnicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tecnicos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TecnicoController {

    private final ITecnicoService tecnicoService;

    @Autowired
    public TecnicoController(ITecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    /**
     * GET /api/tecnicos/{id}/reportes?estado=PROCESO&page=0
     * 
     * Lista los reportes asignados al técnico filtrados por estado
     * 
     * Estados permitidos:
     * - PROCESO: Reportes activos que el técnico está trabajando
     * - RESUELTA: Reportes que el técnico completó y están en auditoría
     * - RECHAZADO_AUDITO: Reportes rechazados que el técnico puede reintentar
     * 
     * Uso: El técnico puede filtrar por estado para ver su lista de actividades
     * - GET /api/tecnicos/2/reportes?estado=PROCESO → Ver reportes activos
     * - GET /api/tecnicos/2/reportes?estado=RECHAZADO_AUDITO → Ver rechazados para
     * reintentar
     * - GET /api/tecnicos/2/reportes?estado=RESUELTA → Ver completados en auditoría
     */
    @GetMapping("/{id}/reportes")
    public ResponseEntity<Page<ReporteDTO>> obtenerReportesAsignados(
            @PathVariable Long id,
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        Page<ReporteDTO> reportes = tecnicoService.obtenerReportesAsignados(id, estado, page);
        return ResponseEntity.ok(reportes);
    }

   
}
