package com.ulima.incidenciaurbana.dto;

import com.ulima.incidenciaurbana.model.Prioridad;

public class TriajeRequest {
    private Prioridad prioridad;
    private Long tecnicoId;

    public TriajeRequest() {
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }
}
