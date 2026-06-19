
BEGIN;


TRUNCATE pagamento, venda, anuncio_carro, usuario_perfil, usuario RESTART IDENTITY CASCADE;

-- ---------- USUÁRIOS ----------
INSERT INTO usuario (codigo, nome, email, senha, nome_usuario, ativo) VALUES
  (1, 'Ricardo Almeida', 'admin@mysummergarage.com', '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'admin',    TRUE),
  (2, 'Bruno Loby',      'bruno@email.com',          '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'bruno',    TRUE),
  (3, 'Carla Mendes',    'carla@email.com',          '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'carla',    TRUE),
  (4, 'Diego Santos',    'diego@email.com',          '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'diego',    TRUE),
  (5, 'Fernanda Lima',   'fernanda@email.com',       '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'fernanda', TRUE),
  (6, 'Gustavo Rocha',   'gustavo@email.com',        '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'gustavo',  TRUE),
  (7, 'Helena Costa',    'helena@email.com',         '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'helena',   TRUE),
  (8, 'Igor Pereira',    'igor@email.com',           '$2b$12$kI.h8TwsivXO.KS21HShu.eh7cj1QnPYDAkLRnKEDkVOwzadBC2d6', 'igor',     FALSE);


INSERT INTO usuario_perfil (codigo_usuario, codigo_perfil) VALUES
  (1, 1),          -- Ricardo  -> ADMIN
  (2, 3),          -- Bruno    -> VENDEDOR
  (3, 3),          -- Carla    -> VENDEDOR
  (4, 3), (4, 2),  -- Diego    -> VENDEDOR + COMPRADOR
  (5, 2),          -- Fernanda -> COMPRADOR
  (6, 2),          -- Gustavo  -> COMPRADOR
  (7, 2),          -- Helena   -> COMPRADOR
  (8, 2);          -- Igor     -> COMPRADOR

-- ---------- ANÚNCIOS DE CARRO ----------
-- status válidos (StatusAnuncio): ATIVO, INATIVO, VENDIDO
INSERT INTO anuncio_carro
  (codigo, titulo, marca, modelo, ano, valor, cor, quilometragem, descricao, data_publicacao, status, codigo_vendedor) VALUES
  (1,  'VW Gol 1.6 completo',          'Volkswagen', 'Gol',      2018,  52000.00, 'Prata',   78000, 'Único dono, revisões em dia, pneus novos.',          DATE '2026-05-02', 'ATIVO',   2),
  (2,  'Honda Civic EXL',              'Honda',      'Civic',    2020, 118000.00, 'Preto',   45000, 'Teto solar, bancos de couro, multimídia original.',  DATE '2026-05-10', 'ATIVO',   2),
  (3,  'Toyota Corolla XEi',           'Toyota',     'Corolla',  2021, 135000.00, 'Branco',  32000, 'Impecável, garantia de fábrica até 2026.',           DATE '2026-04-18', 'VENDIDO', 3),
  (4,  'Fiat Uno Way',                 'Fiat',       'Uno',      2015,  32000.00, 'Vermelho',112000, 'Econômico, ideal para cidade.',                      DATE '2026-05-21', 'ATIVO',   3),
  (5,  'Chevrolet Onix LT',            'Chevrolet',  'Onix',     2019,  68000.00, 'Cinza',    61000, 'Completo, IPVA pago, sem detalhes.',                 DATE '2026-04-25', 'VENDIDO', 4),
  (6,  'Ford Ka SE',                   'Ford',       'Ka',       2017,  41000.00, 'Azul',     89000, 'Bom estado, pequena avaria no para-choque.',         DATE '2026-03-30', 'INATIVO', 4),
  (7,  'Hyundai HB20 Comfort',         'Hyundai',    'HB20',     2022,  89000.00, 'Branco',   18000, 'Seminovo, baixa quilometragem.',                     DATE '2026-05-28', 'ATIVO',   2),
  (8,  'Jeep Renegade Longitude',      'Jeep',       'Renegade', 2021, 142000.00, 'Verde',    40000, 'Diesel 4x4, kit multimídia, rodas de liga.',         DATE '2026-04-12', 'VENDIDO', 3),
  (9,  'Renault Kwid Zen',             'Renault',    'Kwid',     2020,  58000.00, 'Laranja',  37000, 'Compacto, baixo consumo, ótimo custo-benefício.',    DATE '2026-06-01', 'ATIVO',   4),
  (10, 'Nissan Kicks SV',             'Nissan',     'Kicks',    2023, 132000.00, 'Prata',     9000, 'Zero detalhe, ainda na garantia.',                   DATE '2026-06-08', 'ATIVO',   2);

-- ---------- VENDAS ----------
-- status válidos (StatusVenda): PENDENTE, FINALIZADA, CANCELADA
INSERT INTO venda
  (codigo, data_venda, valor_final, status, codigo_anuncio, codigo_comprador) VALUES
  (1, DATE '2026-05-05', 132000.00, 'FINALIZADA', 3, 5),  -- Corolla -> Fernanda
  (2, DATE '2026-05-12',  66500.00, 'FINALIZADA', 5, 6),  -- Onix    -> Gustavo
  (3, DATE '2026-05-20', 140000.00, 'FINALIZADA', 8, 7),  -- Renegade-> Helena
  (4, DATE '2026-06-10',  52000.00, 'PENDENTE',   1, 5),  -- Gol     -> Fernanda
  (5, DATE '2026-06-12',  89000.00, 'CANCELADA',  7, 6),  -- HB20    -> Gustavo
  (6, DATE '2026-06-15', 115000.00, 'PENDENTE',   2, 4);  -- Civic   -> Diego

-- ---------- PAGAMENTOS ----------
-- status válidos (StatusPagamento): PENDENTE, PROCESSANDO, CONCLUIDO, CANCELADO
INSERT INTO pagamento
  (codigo, tipo, parcelas, valor, status, codigo_venda) VALUES
  (1, 'PIX',            1, 132000.00, 'CONCLUIDO',   1),
  (2, 'CARTAO_CREDITO',12,  66500.00, 'CONCLUIDO',   2),
  (3, 'BOLETO',         1, 140000.00, 'CONCLUIDO',   3),
  (4, 'PIX',            1,  52000.00, 'PENDENTE',    4),
  (5, 'CARTAO_CREDITO', 6,  89000.00, 'CANCELADO',   5),
  (6, 'DINHEIRO',       1, 115000.00, 'PROCESSANDO', 6);

-- ---------- AJUSTE DAS SEQUENCES ----------
-- Reposiciona as sequences para o próximo valor após os códigos inseridos manualmente.
SELECT setval('usuario_codigo_seq',       (SELECT MAX(codigo) FROM usuario));
SELECT setval('anuncio_carro_codigo_seq', (SELECT MAX(codigo) FROM anuncio_carro));
SELECT setval('venda_codigo_seq',         (SELECT MAX(codigo) FROM venda));
SELECT setval('pagamento_codigo_seq',     (SELECT MAX(codigo) FROM pagamento));

COMMIT;
