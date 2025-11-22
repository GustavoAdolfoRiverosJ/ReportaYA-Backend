package com.ulima.incidenciaurbana.repository;

import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Reporte;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    @EntityGraph(attributePaths = { "ubicacion", "cuenta", "cuenta.persona" })
    Page<Reporte> findByCuentaId(Long cuentaId, Pageable pageable);

    @EntityGraph(attributePaths = { "ubicacion", "cuenta", "cuenta.persona" })
    Page<Reporte> findByEstado(EstadoReporte estado, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT r FROM Reporte r WHERE " +
            "(:estado IS NULL OR r.estado = :estado) AND " +
            "(:tipo IS NULL OR r.tipo = :tipo) AND " +
            "(:prioridad IS NULL OR r.prioridad = :prioridad)")
    @EntityGraph(attributePaths = { "ubicacion", "cuenta", "cuenta.persona" })
    Page<Reporte> findByFilters(EstadoReporte estado, String tipo, com.ulima.incidenciaurbana.model.Prioridad prioridad,
            Pageable pageable);

    @Override
    @EntityGraph(attributePaths = { "ubicacion", "cuenta", "cuenta.persona" })
    Page<Reporte> findAll(Pageable pageable);
}
