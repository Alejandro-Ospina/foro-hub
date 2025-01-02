ALTER TABLE topicos
ADD CONSTRAINT uq_titulo_topicos UNIQUE (titulo),
ADD CONSTRAINT uq_mensaje_topicos UNIQUE (mensaje);