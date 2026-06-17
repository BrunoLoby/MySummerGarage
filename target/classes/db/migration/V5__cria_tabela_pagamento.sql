CREATE SEQUENCE pagamento_codigo_seq START 1;

CREATE TABLE pagamento (
    codigo BIGINT NOT NULL DEFAULT nextval('pagamento_codigo_seq'),
    tipo VARCHAR(30) NOT NULL,
    parcelas INTEGER NOT NULL DEFAULT 1,
    valor NUMERIC(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    codigo_venda BIGINT NOT NULL,
    PRIMARY KEY (codigo),
    FOREIGN KEY (codigo_venda) REFERENCES venda(codigo)
);