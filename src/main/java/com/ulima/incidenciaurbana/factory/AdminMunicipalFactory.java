package com.ulima.incidenciaurbana.factory;

import com.ulima.incidenciaurbana.model.AdminMunicipal;
import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.Persona;
import org.springframework.stereotype.Component;

/**
 * Factory concreta para crear cuentas de tipo Admin Municipal
 */
@Component
public class AdminMunicipalFactory implements CuentaFactory {

    @Override
    public Cuenta crearCuenta(String usuario, String contrasenaHash, Persona persona) {
        return new AdminMunicipal(usuario, contrasenaHash, persona);
    }

    @Override
    public String getTipoCuenta() {
        return "ADMIN";
    }
}