package com.ulima.incidenciaurbana.service;

public interface IRecuperacionContrasenaService {

    void solicitarRecuperacion(String correo);  

    boolean validarToken(String token);

    void cambiarContrasena(String token, String nueva);
}
