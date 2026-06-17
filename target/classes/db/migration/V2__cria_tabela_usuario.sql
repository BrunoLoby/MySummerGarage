CREATE SEQUENCE usuario_codigo_seq START 1;

CREATE TABLE usuario (
    codigo BIGINT NOT NULL DEFAULT nextval('usuario_codigo_seq'),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    nome_usuario VARCHAR(50) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (codigo)
);

CREATE TABLE usuario_perfil (
    codigo_usuario BIGINT NOT NULL,
    codigo_perfil BIGINT NOT NULL,
    PRIMARY KEY (codigo_usuario, codigo_perfil),
    FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
    FOREIGN KEY (codigo_perfil) REFERENCES perfil(codigo)
);