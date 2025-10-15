package com.ulima.incidenciaurbana.dto;

public class LoginResponse {
    private Long cuentaId;
    private String usuario;
    private String nombreCompleto;
    private String message;

    public LoginResponse(Long cuentaId, String usuario, String nombreCompleto, String message) {
        this.cuentaId = cuentaId;
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.message = message;
    }
    public Long getCuentaId() { return cuentaId; }
    public String getUsuario() { return usuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getMessage() { return message; }
}
