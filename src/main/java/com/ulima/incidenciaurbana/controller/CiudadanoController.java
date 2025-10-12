package com.ulima.incidenciaurbana.controller;

import com.ulima.incidenciaurbana.dto.CiudadanoDTO;
import com.ulima.incidenciaurbana.service.ICiudadanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciudadanos")
@CrossOrigin(origins = "*")
public class CiudadanoController {
    
    private final ICiudadanoService ciudadanoService;
    
    @Autowired
    public CiudadanoController(ICiudadanoService ciudadanoService) {
        this.ciudadanoService = ciudadanoService;
    }
    
    @PostMapping
    public ResponseEntity<CiudadanoDTO> crearCiudadano(@RequestBody CiudadanoDTO ciudadanoDTO) {
        try {
            CiudadanoDTO nuevoCiudadano = ciudadanoService.crearCiudadano(ciudadanoDTO);
            return new ResponseEntity<>(nuevoCiudadano, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CiudadanoDTO> obtenerCiudadano(@PathVariable Long id) {
        try {
            CiudadanoDTO ciudadano = ciudadanoService.obtenerCiudadanoPorId(id);
            return new ResponseEntity<>(ciudadano, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<CiudadanoDTO>> obtenerTodosCiudadanos() {
        List<CiudadanoDTO> ciudadanos = ciudadanoService.obtenerTodosCiudadanos();
        return new ResponseEntity<>(ciudadanos, HttpStatus.OK);
    }
    
    @GetMapping("/dni/{dni}")
    public ResponseEntity<CiudadanoDTO> obtenerCiudadanoPorDni(@PathVariable String dni) {
        try {
            CiudadanoDTO ciudadano = ciudadanoService.obtenerCiudadanoPorDni(dni);
            return new ResponseEntity<>(ciudadano, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CiudadanoDTO> actualizarCiudadano(
            @PathVariable Long id,
            @RequestBody CiudadanoDTO ciudadanoDTO) {
        try {
            CiudadanoDTO ciudadanoActualizado = ciudadanoService.actualizarCiudadano(id, ciudadanoDTO);
            return new ResponseEntity<>(ciudadanoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCiudadano(@PathVariable Long id) {
        try {
            ciudadanoService.eliminarCiudadano(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
