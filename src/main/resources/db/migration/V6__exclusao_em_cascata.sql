-- Ajusta as chaves estrangeiras para ON DELETE CASCADE.
-- Assim, ao excluir um usuário ou um anúncio, o banco apaga
-- automaticamente os registros dependentes (anúncios, vendas e pagamentos),
-- evitando o erro 500 de violação de chave estrangeira.
-- Vínculo usuário <-> perfil (tabela de junção)
ALTER TABLE usuario_perfil
DROP CONSTRAINT usuario_perfil_codigo_usuario_fkey;

ALTER TABLE usuario_perfil ADD CONSTRAINT usuario_perfil_codigo_usuario_fkey FOREIGN KEY (codigo_usuario) REFERENCES usuario (codigo) ON DELETE CASCADE;

-- Anúncio -> vendedor (usuário)
ALTER TABLE anuncio_carro
DROP CONSTRAINT anuncio_carro_codigo_vendedor_fkey;

ALTER TABLE anuncio_carro ADD CONSTRAINT anuncio_carro_codigo_vendedor_fkey FOREIGN KEY (codigo_vendedor) REFERENCES usuario (codigo) ON DELETE CASCADE;

-- Venda -> anúncio
ALTER TABLE venda
DROP CONSTRAINT venda_codigo_anuncio_fkey;

ALTER TABLE venda ADD CONSTRAINT venda_codigo_anuncio_fkey FOREIGN KEY (codigo_anuncio) REFERENCES anuncio_carro (codigo) ON DELETE CASCADE;

-- Venda -> comprador (usuário)
ALTER TABLE venda
DROP CONSTRAINT venda_codigo_comprador_fkey;

ALTER TABLE venda ADD CONSTRAINT venda_codigo_comprador_fkey FOREIGN KEY (codigo_comprador) REFERENCES usuario (codigo) ON DELETE CASCADE;

-- Pagamento -> venda
ALTER TABLE pagamento
DROP CONSTRAINT pagamento_codigo_venda_fkey;

ALTER TABLE pagamento ADD CONSTRAINT pagamento_codigo_venda_fkey FOREIGN KEY (codigo_venda) REFERENCES venda (codigo) ON DELETE CASCADE;