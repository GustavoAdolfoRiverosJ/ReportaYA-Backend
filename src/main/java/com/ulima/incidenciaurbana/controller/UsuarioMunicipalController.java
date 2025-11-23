package com.ulima.incidenciaurbana.controller;

import com.ulima.incidenciaurbana.dto.CrearUsuarioMunicipalDTO;
import com.ulima.incidenciaurbana.dto.UsuarioMunicipalResponseDTO;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.RolMunicipal;
import com.ulima.incidenciaurbana.repository.CuentaRepository;
import com.ulima.incidenciaurbana.service.IUsuarioMunicipalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios-municipales")
public class UsuarioMunicipalController {

    private final IUsuarioMunicipalService usuarioMunicipalService;
    private final CuentaRepository cuentaRepository;

    public UsuarioMunicipalController(IUsuarioMunicipalService usuarioMunicipalService,
                                      CuentaRepository cuentaRepository) {
        this.usuarioMunicipalService = usuarioMunicipalService;
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Crea un usuario municipal (solo ADMIN)
     */
    @PostMapping
    public ResponseEntity<UsuarioMunicipalResponseDTO> crearUsuarioMunicipal(
            @RequestHeader("Cuenta-Id") Long idCreador,
            @RequestBody CrearUsuarioMunicipalDTO dto) {

        // 1. Verificar que la cuenta exista
        Cuenta creador = cuentaRepository.findById(idCreador)
                .orElse(null);

        if (creador == null) {
            // No existe la cuenta que envía la petición
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Verificar que sea ADMIN
        if (creador.getRolMunicipal() != RolMunicipal.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 3. Solo si es ADMIN, puede crear usuarios municipales
        UsuarioMunicipalResponseDTO resp =
                usuarioMunicipalService.crearUsuarioMunicipal(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /**
     * Lista todos los usuarios municipales (solo ADMIN)
     */
    @GetMapping
    public ResponseEntity<List<UsuarioMunicipalResponseDTO>> listarUsuariosMunicipales(
            @RequestHeader("Cuenta-Id") Long idCreador) {

        Cuenta creador = cuentaRepository.findById(idCreador)
                .orElse(null);

        if (creador == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (creador.getRolMunicipal() != RolMunicipal.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<UsuarioMunicipalResponseDTO> lista =
                usuarioMunicipalService.listarUsuariosMunicipales();

        return ResponseEntity.ok(lista);
    }
}