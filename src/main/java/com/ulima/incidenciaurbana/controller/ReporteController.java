package com.ulima.incidenciaurbana.controller;

import com.ulima.incidenciaurbana.dto.ReporteDTO;
import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Prioridad;
import com.ulima.incidenciaurbana.service.IReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final IReporteService reporteService;

    @Autowired
    public ReporteController(IReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping
    public ResponseEntity<ReporteDTO> crearReporte(@RequestBody ReporteDTO reporteDTO) {
        try {
            ReporteDTO nuevoReporte = reporteService.crearReporte(reporteDTO);
            return new ResponseEntity<>(nuevoReporte, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtenerReporte(@PathVariable Long id) {
        try {
            ReporteDTO reporte = reporteService.obtenerReportePorId(id);
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> obtenerTodosReportes() {
        List<ReporteDTO> reportes = reporteService.obtenerTodosReportes();
        return new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<ReporteDTO>> obtenerReportesPorCuenta(@PathVariable Long cuentaId) {
        List<ReporteDTO> reportes = reporteService.obtenerReportesPorCuenta(cuentaId);
        return new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReporteDTO>> obtenerReportesPorEstado(@PathVariable EstadoReporte estado) {
        List<ReporteDTO> reportes = reporteService.obtenerReportesPorEstado(estado);
        return new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteDTO> actualizarReporte(
            @PathVariable Long id,
            @RequestBody ReporteDTO reporteDTO) {
        try {
            ReporteDTO reporteActualizado = reporteService.actualizarReporte(id, reporteDTO);
            return new ResponseEntity<>(reporteActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReporteDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoReporte nuevoEstado) {
        try {
            ReporteDTO reporteActualizado = reporteService.cambiarEstadoReporte(id, nuevoEstado);
            return new ResponseEntity<>(reporteActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/prioridad")
    public ResponseEntity<ReporteDTO> cambiarPrioridad(
            @PathVariable Long id,
            @RequestParam Prioridad nuevaPrioridad) {
        try {
            ReporteDTO reporteActualizado = reporteService.cambiarPrioridadReporte(id, nuevaPrioridad);
            return new ResponseEntity<>(reporteActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        try {
            reporteService.eliminarReporte(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/triaje")
    public ResponseEntity<ReporteDTO> triajeYAsignar(
            @PathVariable Long id,
            @RequestBody com.ulima.incidenciaurbana.dto.TriajeRequest triajeRequest,
            @RequestParam(required = false) Long operadorId) {
        try {
            ReporteDTO reporteActualizado = reporteService.triajeYAsignar(id, operadorId, triajeRequest);
            return new ResponseEntity<>(reporteActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
