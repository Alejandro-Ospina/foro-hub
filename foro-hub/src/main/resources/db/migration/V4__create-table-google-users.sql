-- Crear tabla de usuarios base
CREATE TABLE usuarios_base
(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Crear tabla de usuarios de google en la db
CREATE TABLE usuarios_google
(
    id SERIAL PRIMARY KEY,
    sub VARCHAR(255) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    foto VARCHAR(500) NOT NULL,
    activo BOOLEAN NOT NULL
);

-- Copiar datos de usuarios locales a la tabla base
INSERT INTO usuarios_base (id, nombre, email)
SELECT id, nombre, email FROM usuario;

-- Agregar llave foranea heredada de id de usuarios base a usuarios
ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_usuarios_base
FOREIGN KEY (id) REFERENCES usuarios_base (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- Agregar llave foranea heredada de id de usuarios base a usuarios de google
ALTER TABLE usuarios_google
ADD CONSTRAINT fk_usuarios_google_usuarios_base
FOREIGN KEY (id) REFERENCES usuarios_base (id)
ON DELETE CASCADE
ON UPDATE CASCADE;