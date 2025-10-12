package com.ulima.incidenciaurbana.repository;

import com.ulima.incidenciaurbana.model.EstadoReporte;
import com.ulima.incidenciaurbana.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByEstado(EstadoReporte estado);
    List<Reporte> findByCuentaId(Long cuentaId);
    List<Reporte> findByCuentaIdAndEstado(Long cuentaId, EstadoReporte estado);
}
