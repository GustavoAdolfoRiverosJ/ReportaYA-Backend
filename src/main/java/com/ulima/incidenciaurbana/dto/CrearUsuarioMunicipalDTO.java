package com.ulima.incidenciaurbana.dto;

import com.ulima.incidenciaurbana.model.RolMunicipal;

public class CrearUsuarioMunicipalDTO {
    
    private String usuario;
    private String password;
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String correo;
    private RolMunicipal rolMunicipal;


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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


}