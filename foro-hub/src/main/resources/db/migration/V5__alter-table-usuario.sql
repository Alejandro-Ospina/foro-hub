-- Eliminar las columnas innecesarias de la tabla usuario
ALTER TABLE usuario
DROP COLUMN nombre;

ALTER TABLE usuario
DROP COLUMN email;
