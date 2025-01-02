-- Crer tabla de cursos
CREATE TABLE cursos
(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    categoria VARCHAR(50) NOT NULL
);

-- Crear tabla de topicos
CREATE TABLE topicos
(
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    mensaje VARCHAR(1000) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    curso_id INT NOT NULL,
    usuario_id INT NOT NULL
);

-- Crear tabla respuesta
CREATE TABLE respuesta
(
    id SERIAL PRIMARY KEY,
    mensaje VARCHAR(1000) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    solucion TEXT,
    topico_id INT NOT NULL,
    usuario_id INT NOT NULL
);

-- Crear tabla de usuarios
CREATE TABLE usuario
(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Crear tabla de perfiles
CREATE TABLE perfil
(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Crear tabla intermedia para relacion many to many entre perfil y usuario
CREATE TABLE usuario_perfil (
    usuario_id INT NOT NULL,
    perfil_id INT NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_perfil FOREIGN KEY (perfil_id) REFERENCES perfil (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- Relacion one to many entre cursos y topicos
ALTER TABLE topicos
ADD CONSTRAINT fk_curso_topicos
FOREIGN KEY (curso_id) REFERENCES cursos (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- Relacion one to many entre usuarios y topicos
ALTER TABLE topicos
ADD CONSTRAINT fk_usuario_topico
FOREIGN KEY (usuario_id) REFERENCES usuario (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- Relacion one to many entre topicos y respuestas
ALTER TABLE respuesta
ADD CONSTRAINT fk_topico_respuesta
FOREIGN KEY (topico_id) REFERENCES topicos (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- Relacion one to many entre usuarios y respuestas
ALTER TABLE respuesta
ADD CONSTRAINT fk_usuario_respuesta
FOREIGN KEY (usuario_id) REFERENCES usuario (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- Crear índices para claves foráneas
CREATE INDEX idx_topicos_curso_id ON topicos (curso_id);
CREATE INDEX idx_topicos_usuario_id ON topicos (usuario_id);
CREATE INDEX idx_respuesta_topico_id ON respuesta (topico_id);
CREATE INDEX idx_respuesta_usuario_id ON respuesta (usuario_id);