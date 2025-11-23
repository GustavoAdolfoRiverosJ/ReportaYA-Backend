package com.ulima.incidenciaurbana.dto;

import com.ulima.incidenciaurbana.model.RolMunicipal;

public class UsuarioMunicipalResponseDTO {
    
    private Long id;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private RolMunicipal rolMunicipal;
    private boolean activo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public RolMunicipal getRolMunicipal() {
        return rolMunicipal;
    }

    public void setRolMunicipal(RolMunicipal rolMunicipal) {
        this.rolMunicipal = rolMunicipal;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}