package com.ulima.incidenciaurbana.service;

public interface IEmailService {

    void enviarCorreoRecuperacionContrasena(String correoDestino, String enlaceRecuperacion);
}
