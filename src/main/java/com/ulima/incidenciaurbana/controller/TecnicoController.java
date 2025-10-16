package com.ulima.incidenciaurbana.controller;

import com.ulima.incidenciaurbana.dto.TecnicoDTO;
import com.ulima.incidenciaurbana.service.ITecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tecnicos")
@CrossOrigin(origins = "*")
public class TecnicoController {

    private final ITecnicoService tecnicoService;

    @Autowired
    public TecnicoController(ITecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    /**
     * GET /api/tecnicos?page=0 - Lista paginada de técnicos (10 por página)
     */
    @GetMapping
    public ResponseEntity<Page<TecnicoDTO>> obtenerTodosTecnicos(
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<TecnicoDTO> pageResult = tecnicoService.obtenerTodosTecnicos(page);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }
}
