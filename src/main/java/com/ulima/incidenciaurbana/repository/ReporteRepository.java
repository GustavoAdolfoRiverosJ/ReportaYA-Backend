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
    
    @EntityGraph(attributePaths = {"ubicacion", "cuenta", "cuenta.persona"})
    Page<Reporte> findByCuentaId(Long cuentaId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"ubicacion", "cuenta", "cuenta.persona"})
    Page<Reporte> findByEstado(EstadoReporte estado, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"ubicacion", "cuenta", "cuenta.persona"})
    Page<Reporte> findAll(Pageable pageable);
}
