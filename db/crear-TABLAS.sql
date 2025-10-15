-- DDL para crear las tablas básicas del proyecto ReportaYA
-- Ejecutar en la base de datos `reportaya` desde pgAdmin (Query Tool) como un usuario con permisos.

-- 1) Personas
CREATE TABLE IF NOT EXISTS personas (
  id BIGSERIAL PRIMARY KEY,
  nombres VARCHAR(255) NOT NULL,
  apellidos VARCHAR(255) NOT NULL,
  dni VARCHAR(50) NOT NULL UNIQUE,
  telefono VARCHAR(50) NOT NULL,
  correo VARCHAR(255) NOT NULL UNIQUE
);

-- 2) Cuentas (tabla base para herencia JOINED)
CREATE TABLE IF NOT EXISTS cuentas (
  id BIGSERIAL PRIMARY KEY,
  usuario VARCHAR(255) NOT NULL UNIQUE,
  contrasena_hash VARCHAR(255) NOT NULL,
  persona_id BIGINT NOT NULL UNIQUE,
  fecha_creacion TIMESTAMP WITHOUT TIME ZONE,
  fecha_actualizacion TIMESTAMP WITHOUT TIME ZONE,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_cuentas_persona FOREIGN KEY (persona_id) REFERENCES personas (id) ON DELETE RESTRICT
);

-- 3) Tablas de subclases (JOINED): operadores_municipales, tecnicos, ciudadanos
CREATE TABLE IF NOT EXISTS operadores_municipales (
  id BIGINT PRIMARY KEY,
  CONSTRAINT fk_op_mun_cuenta FOREIGN KEY (id) REFERENCES cuentas (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tecnicos (
  id BIGINT PRIMARY KEY,
  CONSTRAINT fk_tecnicos_cuenta FOREIGN KEY (id) REFERENCES cuentas (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ciudadanos (
  id BIGINT PRIMARY KEY,
  CONSTRAINT fk_ciudadanos_cuenta FOREIGN KEY (id) REFERENCES cuentas (id) ON DELETE CASCADE
);

-- 4) Reportes
CREATE TABLE IF NOT EXISTS reportes (
  id BIGSERIAL PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  descripcion VARCHAR(1000) NOT NULL,
  cuenta_id BIGINT NOT NULL,
  prioridad VARCHAR(50),
  estado VARCHAR(50) NOT NULL,
  fecha_creacion TIMESTAMP WITHOUT TIME ZONE,
  fecha_actualizacion TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT fk_reportes_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas (id) ON DELETE CASCADE
);

-- 5) Asignaciones
CREATE TABLE IF NOT EXISTS asignaciones (
  id BIGSERIAL PRIMARY KEY,
  reporte_id BIGINT NOT NULL,
  operador_id BIGINT NOT NULL,
  tecnico_id BIGINT,
  fecha_asignacion TIMESTAMP WITHOUT TIME ZONE,
  fecha_cierre TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT fk_asignaciones_reporte FOREIGN KEY (reporte_id) REFERENCES reportes (id) ON DELETE CASCADE,
  CONSTRAINT fk_asignaciones_operador FOREIGN KEY (operador_id) REFERENCES operadores_municipales (id) ON DELETE RESTRICT,
  CONSTRAINT fk_asignaciones_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnicos (id) ON DELETE SET NULL
);

-- 6) Índice parcial recomendado para impedir más de una asignación abierta por reporte
-- (evita dos filas con fecha_cierre IS NULL para el mismo reporte)
CREATE UNIQUE INDEX IF NOT EXISTS ux_asignaciones_reporte_abierta ON asignaciones (reporte_id) WHERE fecha_cierre IS NULL;

-- 7) Índices frecuentes first-come wins y consultas comunes
-- (mejoran rendimiento en búsquedas y joins)
CREATE INDEX IF NOT EXISTS idx_reportes_cuenta_id ON reportes (cuenta_id);
CREATE INDEX IF NOT EXISTS idx_asignaciones_reporte_id ON asignaciones (reporte_id);
CREATE INDEX IF NOT EXISTS idx_asignaciones_tecnico_id ON asignaciones (tecnico_id);


DROP TABLE IF EXISTS asignaciones CASCADE;
DROP TABLE IF EXISTS reportes CASCADE;
DROP TABLE IF EXISTS operadores_municipales CASCADE;
DROP TABLE IF EXISTS tecnicos CASCADE;
DROP TABLE IF EXISTS ciudadanos CASCADE;
DROP TABLE IF EXISTS cuentas CASCADE;
DROP TABLE IF EXISTS personas CASCADE;