-- Trunca tablas relevantes, reinicia sequences y crea 3 personas con su cuenta y su rol

TRUNCATE TABLE
  asignaciones,
  reportes,
  operadores_municipales,
  tecnicos,
  ciudadanos,
  cuentas,
  personas
RESTART IDENTITY CASCADE;

-- 1) Operador Municipal (Ana Garcia)
WITH p AS (
  INSERT INTO personas (nombres, apellidos, dni, telefono, correo)
  VALUES ('Ana', 'Garcia', '73456789', '999000111', 'ana.garcia@example.com')
  ON CONFLICT (dni) DO UPDATE SET nombres = EXCLUDED.nombres, apellidos = EXCLUDED.apellidos, telefono = EXCLUDED.telefono, correo = EXCLUDED.correo
  RETURNING id
)
, c AS (
  INSERT INTO cuentas (usuario, contrasena_hash, persona_id, fecha_creacion, fecha_actualizacion, activo)
  SELECT 'op_ana', 'hash_demo', id, now(), now(), true FROM p
  ON CONFLICT (persona_id) DO NOTHING
  RETURNING id
)
INSERT INTO operadores_municipales (id)
SELECT id FROM c;

-- Fallback: si la cuenta ya existía, asegurar la fila en operadores_municipales
INSERT INTO operadores_municipales (id)
SELECT c2.id FROM cuentas c2
JOIN personas p2 ON p2.id = c2.persona_id
WHERE p2.dni = '73456789'
ON CONFLICT DO NOTHING;

-- 2) Tecnico (Luis Torres)
WITH p AS (
  INSERT INTO personas (nombres, apellidos, dni, telefono, correo)
  VALUES ('Luis', 'Torres', '71234567', '988777666', 'luis.torres@example.com')
  ON CONFLICT (dni) DO UPDATE SET nombres = EXCLUDED.nombres, apellidos = EXCLUDED.apellidos, telefono = EXCLUDED.telefono, correo = EXCLUDED.correo
  RETURNING id
)
, c AS (
  INSERT INTO cuentas (usuario, contrasena_hash, persona_id, fecha_creacion, fecha_actualizacion, activo)
  SELECT 'tec_luis', 'hash_demo', id, now(), now(), true FROM p
  ON CONFLICT (persona_id) DO NOTHING
  RETURNING id
)
INSERT INTO tecnicos (id)
SELECT id FROM c;

INSERT INTO tecnicos (id)
SELECT c2.id FROM cuentas c2
JOIN personas p2 ON p2.id = c2.persona_id
WHERE p2.dni = '71234567'
ON CONFLICT DO NOTHING;

-- 3) Ciudadano (Carlos Perez)
WITH p AS (
  INSERT INTO personas (nombres, apellidos, dni, telefono, correo)
  VALUES ('Carlos', 'Perez', '70011223', '977666555', 'carlos.perez@example.com')
  ON CONFLICT (dni) DO UPDATE SET nombres = EXCLUDED.nombres, apellidos = EXCLUDED.apellidos, telefono = EXCLUDED.telefono, correo = EXCLUDED.correo
  RETURNING id
)
, c AS (
  INSERT INTO cuentas (usuario, contrasena_hash, persona_id, fecha_creacion, fecha_actualizacion, activo)
  SELECT 'ci_carlos', 'hash_demo', id, now(), now(), true FROM p
  ON CONFLICT (persona_id) DO NOTHING
  RETURNING id
)
INSERT INTO ciudadanos (id)
SELECT id FROM c;

INSERT INTO ciudadanos (id)
SELECT c2.id FROM cuentas c2
JOIN personas p2 ON p2.id = c2.persona_id
WHERE p2.dni = '70011223'
ON CONFLICT DO NOTHING;

-- Consultas de verificación
SELECT p.id AS persona_id, p.dni, p.nombres, c.id AS cuenta_id, c.usuario,
  CASE
    WHEN o.id IS NOT NULL THEN 'OPERADOR'
    WHEN t.id IS NOT NULL THEN 'TECNICO'
    WHEN ci.id IS NOT NULL THEN 'CIUDADANO'
    ELSE 'SIN_ROL'
  END AS rol
FROM personas p
LEFT JOIN cuentas c ON c.persona_id = p.id
LEFT JOIN operadores_municipales o ON o.id = c.id
LEFT JOIN tecnicos t ON t.id = c.id
LEFT JOIN ciudadanos ci ON ci.id = c.id
WHERE p.dni IN ('73456789','71234567','70011223')
ORDER BY p.id;


-- Consulta de todas las tablas
SELECT * FROM cuentas;
SELECT * FROM ciudadanos;
SELECT * FROM operadores_municipales;
SELECT * FROM tecnicos;
SELECT * FROM reportes;
SELECT * FROM asignaciones;
SELECT * FROM personas;