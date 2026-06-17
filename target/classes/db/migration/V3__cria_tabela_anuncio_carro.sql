CREATE SEQUENCE anuncio_carro_codigo_seq START 1;

CREATE TABLE anuncio_carro (
    codigo BIGINT NOT NULL DEFAULT nextval('anuncio_carro_codigo_seq'),
    titulo VARCHAR(150) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INTEGER NOT NULL,
    valor NUMERIC(12,2) NOT NULL,
    cor VARCHAR(30),
    quilometragem INTEGER,
    descricao VARCHAR(2000),
    data_publicacao DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    codigo_vendedor BIGINT NOT NULL,
    PRIMARY KEY (codigo),
    FOREIGN KEY (codigo_vendedor) REFERENCES usuario(codigo)
);