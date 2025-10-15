package com.ulima.incidenciaurbana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "operadores_municipales")
public class OperadorMunicipal extends Cuenta {

    public OperadorMunicipal() {
        super();
    }

    public OperadorMunicipal(String usuario, String contrasenaHash, Persona persona) {
        super(usuario, contrasenaHash, persona);
    }

    /**
     * Operaciones de dominio del operador municipal
     * public void asignarPrioridad(Reporte reporte, Prioridad prioridad) {
     * if (reporte != null && prioridad != null) {
     * reporte.setPrioridad(prioridad);
     * }
     * }
     * 
     * public Asignacion asignarTecnico(Reporte reporte, Tecnico tecnico) {
     * Asignacion asignacion = new Asignacion(reporte, this, tecnico);
     * return asignacion;
     * }
     **/
}
