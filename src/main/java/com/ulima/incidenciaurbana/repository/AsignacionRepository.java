package com.ulima.incidenciaurbana.repository;

import com.ulima.incidenciaurbana.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    
    /**
     * Busca una asignación activa (sin fecha de cierre) para un reporte específico
     */
    
    /**
     * Busca todas las asignaciones de un reporte específico
     */
    
    /**
     * Busca todas las asignaciones realizadas por un operador
     */
    
    /**
     * Busca todas las asignaciones de un técnico
     */
    
    /**
     * Busca todas las asignaciones activas (sin fecha de cierre)
     */
    Optional<Asignacion> findByReporteIdAndFechaCierreIsNull(Long reporteId);
}
