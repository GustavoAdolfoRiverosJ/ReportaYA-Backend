package com.ulima.incidenciaurbana.dto;

import java.time.LocalDateTime;

import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Prioridad;

public class ReporteDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String urlFoto;
    private Long cuentaId;
    private String nombreCiudadano;
    private Prioridad prioridad;
    private EstadoReporte estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private UbicacionDTO ubicacion;

    // Constructors
    public ReporteDTO() {
    }

    public ReporteDTO(Long id, String titulo, String descripcion, Long cuentaId, String nombreCiudadano,
            Prioridad prioridad, EstadoReporte estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cuentaId = cuentaId;
        this.nombreCiudadano = nombreCiudadano;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public ReporteDTO(Long id, String titulo, String descripcion, Long cuentaId, String nombreCiudadano,
            Prioridad prioridad, EstadoReporte estado, UbicacionDTO ubicacion, LocalDateTime fechaCreacion,
            LocalDateTime fechaActualizacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cuentaId = cuentaId;
        this.nombreCiudadano = nombreCiudadano;
        this.prioridad = prioridad;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNombreCiudadano() {
        return nombreCiudadano;
    }

    public void setNombreCiudadano(String nombreCiudadano) {
        this.nombreCiudadano = nombreCiudadano;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoReporte getEstado() {
        return estado;
    }

    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public UbicacionDTO getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionDTO ubicacion) {
        this.ubicacion = ubicacion;
    }
}
