package com.ulima.incidenciaurbana.repository;

import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    Page<Reporte> findByCuentaId(Long cuentaId, Pageable pageable);

    // Query normal sin JOIN FETCH (Spring Data manejará la paginación)
    Page<Reporte> findByEstado(EstadoReporte estado, Pageable pageable);

    // Query con JOIN FETCH para cargar relaciones (sin paginación)
    @Query("SELECT DISTINCT r FROM Reporte r " +
            "LEFT JOIN FETCH r.cuenta c " +
            "LEFT JOIN FETCH c.persona " +
            "LEFT JOIN FETCH r.ubicacion " +
            "WHERE r.estado = :estado " +
            "ORDER BY r.fechaCreacion DESC")
    List<Reporte> findByEstadoWithDetails(@Param("estado") EstadoReporte estado);

    // Query para obtener reporte por ID con todas las relaciones cargadas
    // Evita problema de "Duplicate row" al acceder a relaciones lazy
    @Query("SELECT DISTINCT r FROM Reporte r " +
            "LEFT JOIN FETCH r.cuenta c " +
            "LEFT JOIN FETCH c.persona " +
            "LEFT JOIN FETCH r.ubicacion " +
            "LEFT JOIN FETCH r.asignaciones a " +
            "LEFT JOIN FETCH a.tecnico " +
            "WHERE r.id = :id")
    Optional<Reporte> findByIdWithDetails(@Param("id") Long id);
}
