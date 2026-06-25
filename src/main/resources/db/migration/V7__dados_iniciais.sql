-- Perfis simplificados
INSERT INTO perfil (codigo, nome)
SELECT nextval('perfil_codigo_seq'), 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM perfil WHERE nome = 'ADMIN');

INSERT INTO perfil (codigo, nome)
SELECT nextval('perfil_codigo_seq'), 'USER'
WHERE NOT EXISTS (SELECT 1 FROM perfil WHERE nome = 'USER');

-- Usuario ADMIN
INSERT INTO usuario (codigo, nome, email, senha, nome_usuario, ativo)
SELECT nextval('usuario_codigo_seq'), 'Administrador', 'admin@msg.com',
       '$2a$10$OKqmqudfz9M3OTtSP3B.e.xiBXfymq2PjLfcuQd1GvsYjkrpmibxq', 'admin', TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE nome_usuario = 'admin' OR email = 'admin@msg.com'
);

-- Usuario normal (comprador/vendedor)
INSERT INTO usuario (codigo, nome, email, senha, nome_usuario, ativo)
SELECT nextval('usuario_codigo_seq'), 'Bruno Loby', 'bruno@msg.com',
       '$2a$10$OKqmqudfz9M3OTtSP3B.e.xiBXfymq2PjLfcuQd1GvsYjkrpmibxq', 'bruno', TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE nome_usuario = 'bruno' OR email = 'bruno@msg.com'
);

-- Vínculos
INSERT INTO usuario_perfil (codigo_usuario, codigo_perfil)
SELECT u.codigo, p.codigo FROM usuario u, perfil p
WHERE u.nome_usuario = 'admin' AND p.nome = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM usuario_perfil up WHERE up.codigo_usuario = u.codigo AND up.codigo_perfil = p.codigo
  );

INSERT INTO usuario_perfil (codigo_usuario, codigo_perfil)
SELECT u.codigo, p.codigo FROM usuario u, perfil p
WHERE u.nome_usuario = 'bruno' AND p.nome = 'USER'
  AND NOT EXISTS (
      SELECT 1 FROM usuario_perfil up WHERE up.codigo_usuario = u.codigo AND up.codigo_perfil = p.codigo
  );

-- Anúncios de exemplo
INSERT INTO anuncio_carro
    (codigo, titulo, marca, modelo, ano, valor, cor, quilometragem, descricao, data_publicacao, status, codigo_vendedor)
SELECT nextval('anuncio_carro_codigo_seq'), 'Dodge Viper GTS', 'Dodge', 'Viper', 1996,
       115000.00, 'Azul', 45000, 'Unico dono, revisoes em dia, pneus novos.', CURRENT_DATE, 'ATIVO',
       (SELECT codigo FROM usuario WHERE nome_usuario = 'bruno')
WHERE NOT EXISTS (SELECT 1 FROM anuncio_carro WHERE titulo = 'Dodge Viper GTS');

INSERT INTO anuncio_carro
    (codigo, titulo, marca, modelo, ano, valor, cor, quilometragem, descricao, data_publicacao, status, codigo_vendedor)
SELECT nextval('anuncio_carro_codigo_seq'), 'Volkswagen Golf GTI 2019', 'Volkswagen', 'Golf', 2019,
       135000.00, 'Branco', 60000, 'Turbo, interior em couro, multimidia.', CURRENT_DATE, 'ATIVO',
       (SELECT codigo FROM usuario WHERE nome_usuario = 'bruno')
WHERE NOT EXISTS (SELECT 1 FROM anuncio_carro WHERE titulo = 'Volkswagen Golf GTI 2019');

INSERT INTO anuncio_carro
    (codigo, titulo, marca, modelo, ano, valor, cor, quilometragem, descricao, data_publicacao, status, codigo_vendedor)
SELECT nextval('anuncio_carro_codigo_seq'), 'Astra 2.0', 'Opel', 'Astra', 2021,
       128000.00, 'Prata', 30000, 'Automatico, baixa quilometragem, garantia de fabrica.', CURRENT_DATE, 'ATIVO',
       (SELECT codigo FROM usuario WHERE nome_usuario = 'bruno')
WHERE NOT EXISTS (SELECT 1 FROM anuncio_carro WHERE titulo = 'Astra 2.0');

INSERT INTO anuncio_carro
    (codigo, titulo, marca, modelo, ano, valor, cor, quilometragem, descricao, data_publicacao, status, codigo_vendedor)
SELECT nextval('anuncio_carro_codigo_seq'), 'Jeep Compass Longitude 2018', 'Jeep', 'Compass', 2018,
       98000.00, 'Azul', 78000, 'Diesel 4x4, teto solar, bem conservado.', CURRENT_DATE, 'ATIVO',
       (SELECT codigo FROM usuario WHERE nome_usuario = 'bruno')
WHERE NOT EXISTS (SELECT 1 FROM anuncio_carro WHERE titulo = 'Jeep Compass Longitude 2018');