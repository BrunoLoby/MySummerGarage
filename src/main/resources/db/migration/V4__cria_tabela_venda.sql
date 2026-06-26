CREATE SEQUENCE venda_codigo_seq START 1;

CREATE TABLE
    venda (
        codigo BIGINT NOT NULL DEFAULT nextval ('venda_codigo_seq'),
        data_venda DATE NOT NULL,
        valor_final NUMERIC(12, 2) NOT NULL,
        status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
        codigo_anuncio BIGINT NOT NULL,
        codigo_comprador BIGINT NOT NULL,
        PRIMARY KEY (codigo),
        FOREIGN KEY (codigo_anuncio) REFERENCES anuncio_carro (codigo),
        FOREIGN KEY (codigo_comprador) REFERENCES usuario (codigo)
    );