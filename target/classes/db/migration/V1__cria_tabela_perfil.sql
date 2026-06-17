CREATE SEQUENCE perfil_codigo_seq START 1;

CREATE TABLE perfil (
    codigo BIGINT NOT NULL DEFAULT nextval('perfil_codigo_seq'),
    nome VARCHAR(50) NOT NULL,
    PRIMARY KEY (codigo)
);

INSERT INTO perfil (codigo, nome) VALUES (nextval('perfil_codigo_seq'), 'ADMIN');
INSERT INTO perfil (codigo, nome) VALUES (nextval('perfil_codigo_seq'), 'COMPRADOR');
INSERT INTO perfil (codigo, nome) VALUES (nextval('perfil_codigo_seq'), 'VENDEDOR');