package com.ulima.incidenciaurbana.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reportes")
public class Reporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(nullable = false, length = 1000)
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;
    
    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReporte estado;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Constructors
    public Reporte() {
        this.estado = EstadoReporte.PENDIENTE;
        this.prioridad = Prioridad.MEDIA;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Reporte(String titulo, String descripcion, Cuenta cuenta) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cuenta = cuenta;
    }
    
    // Business Methods
    public void cambiarEstado(EstadoReporte nuevoEstado) {
        this.estado = nuevoEstado;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void cambiarPrioridad(Prioridad nuevaPrioridad) {
        this.prioridad = nuevaPrioridad;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
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
    
    public Cuenta getCuenta() {
        return cuenta;
    }
    
    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reporte)) return false;
        Reporte reporte = (Reporte) o;
        return Objects.equals(id, reporte.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
