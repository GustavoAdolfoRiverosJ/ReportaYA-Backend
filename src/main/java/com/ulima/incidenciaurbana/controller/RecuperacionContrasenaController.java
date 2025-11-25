package com.ulima.incidenciaurbana.controller;

import com.ulima.incidenciaurbana.dto.ResetContrasenaDTO;
import com.ulima.incidenciaurbana.dto.SolicitarRecuperacionContrasenaDTO;
import com.ulima.incidenciaurbana.service.IRecuperacionContrasenaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
public class RecuperacionContrasenaController {

    private final IRecuperacionContrasenaService recuperacionService;

    public RecuperacionContrasenaController(IRecuperacionContrasenaService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }

    /**
     * Escenario 1: Solicitud de recuperación
     */
    @PostMapping("/forgot")
    public ResponseEntity<?> solicitarRecuperacion(
            @Valid @RequestBody SolicitarRecuperacionContrasenaDTO request) {

        recuperacionService.solicitarRecuperacion(request.getCorreo());
        // Aunque el correo no exista, respondemos 200 para no revelar usuarios
        return ResponseEntity.ok("Si el correo existe, se ha enviado un enlace de recuperación.");
    }

    /**
     * Escenario 2 y 4: Validar enlace
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validarToken(@RequestParam("token") String token) {
        boolean valido = recuperacionService.validarToken(token);

        if (!valido) {
            return ResponseEntity.badRequest().body("El enlace de recuperación no es válido o ha expirado.");
        }

        return ResponseEntity.ok("Token válido.");
    }

    /**
     * Escenario 3: Cambiar contraseña
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetearContrasena(
            @Valid @RequestBody ResetContrasenaDTO request) {

        recuperacionService.cambiarContrasena(request.getToken(), request.getNuevaContrasena());
        return ResponseEntity.ok("Contraseña cambiada exitosamente.");
    }
}
