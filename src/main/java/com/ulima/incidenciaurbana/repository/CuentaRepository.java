package com.ulima.incidenciaurbana.repository;

import com.ulima.incidenciaurbana.model.Cuenta;
import com.ulima.incidenciaurbana.model.RolMunicipal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByUsuario(String usuario);
    Optional<Cuenta> findByUsuarioAndActivoTrue(String usuario);
    boolean existsByUsuario(String usuario);

    // lista todos los usuarios que tienen algún rol municipal
    List<Cuenta> findByRolMunicipalIsNotNull();
    
    // lista por rol específico (OPERADOR_MUNICIPAL, TECNICO_CAMPO, ADMIN)
    List<Cuenta> findByRolMunicipal(RolMunicipal rolMunicipal);
}