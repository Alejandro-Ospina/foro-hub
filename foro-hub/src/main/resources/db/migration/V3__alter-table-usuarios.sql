-- Se agrega columna de usuario activo
ALTER TABLE usuario ADD COLUMN activo BOOLEAN DEFAULT TRUE NOT NULL;

-- Se actualizan los usuarios ya creados en la DB
UPDATE usuario SET activo = TRUE;