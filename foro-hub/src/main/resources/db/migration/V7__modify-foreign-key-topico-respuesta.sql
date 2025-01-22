-- Cambiar llave foranea en respuesta
ALTER TABLE respuesta
ADD CONSTRAINT fk_usuarios_base_respuesta
FOREIGN KEY (usuario_id) REFERENCES usuarios_base (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- Cambiar llave foranea en topico
ALTER TABLE topicos
ADD CONSTRAINT fk_usuarios_base_topicos
FOREIGN KEY (usuario_id) REFERENCES usuarios_base (id)
ON DELETE CASCADE
ON UPDATE CASCADE;