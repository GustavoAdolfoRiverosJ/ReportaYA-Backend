package com.ulima.incidenciaurbana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins_municipales")
public class AdminMunicipal extends Cuenta {

    public AdminMunicipal() {
        super();
        // este campo viene de Cuenta
        setRolMunicipal(RolMunicipal.ADMIN);
    }

    public AdminMunicipal(String usuario, String contrasenaHash, Persona persona) {
        super(usuario, contrasenaHash, persona);
        setRolMunicipal(RolMunicipal.ADMIN);
    }

    @Override
    public String getTipoCuenta() {
        return "ADMIN";
    }
}