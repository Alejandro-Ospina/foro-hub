-- Eliminar las columnas innecesarias de la tabla usuarios_google
ALTER TABLE usuarios_google
DROP COLUMN nombre;

ALTER TABLE usuarios_google
DROP COLUMN email;