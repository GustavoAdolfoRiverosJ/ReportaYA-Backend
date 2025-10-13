package com.ulima.incidenciaurbana.repository;

import com.ulima.incidenciaurbana.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    Optional<Asignacion> findByReporteIdAndFechaCierreIsNull(Long reporteId);
}
