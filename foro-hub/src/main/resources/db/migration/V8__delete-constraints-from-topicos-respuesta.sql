-- Se elimina restricción que apunta a usuario
ALTER TABLE topicos
DROP CONSTRAINT fk_usuario_topico;

-- Se elimina restricción que apunta a respuesta
ALTER TABLE respuesta
DROP CONSTRAINT fk_usuario_respuesta;