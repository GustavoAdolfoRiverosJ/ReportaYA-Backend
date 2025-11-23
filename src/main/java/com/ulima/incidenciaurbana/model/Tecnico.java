package com.ulima.incidenciaurbana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tecnicos")
public class Tecnico extends Cuenta {

    public Tecnico() {
        super();
        setRolMunicipal(RolMunicipal.TECNICO_CAMPO);
    }

    @Override
    public String getTipoCuenta() {
        return "TECNICO";
    }

    public Tecnico(String usuario, String contrasenaHash, Persona persona) {
        super(usuario, contrasenaHash, persona);
        setRolMunicipal(RolMunicipal.TECNICO_CAMPO);
    }

    /*
     * public void completarAsignacion(Asignacion asignacion) {
     * if (asignacion != null) {
     * asignacion.setFechaCierre(java.time.LocalDateTime.now());
     * }
     * }
     */
}