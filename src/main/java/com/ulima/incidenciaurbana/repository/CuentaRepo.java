package com.ulima.incidenciaurbana.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ulima.incidenciaurbana.model.Cuenta;

public interface CuentaRepo extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByUsuarioAndActivoTrue(String usuario);
}
