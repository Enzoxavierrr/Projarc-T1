-- ================================================
-- CLIENTES
-- ================================================
INSERT INTO clientes (cpf, nome, email, senha, celular, endereco) VALUES ('9001', 'Huguinho Pato', 'huguinho.pato@email.com', '123456', '51985744566', 'Rua das Flores, 100');
INSERT INTO clientes (cpf, nome, email, senha, celular, endereco) VALUES ('9002', 'Luizinho Pato', 'luizinho.pato@email.com', '123456', '51991720799', 'Av. Central, 200');
INSERT INTO clientes (cpf, nome, email, senha, celular, endereco) VALUES ('11111111111', 'Maria Souza', 'maria.souza@email.com', '123456', '51988880001', 'Rua das Acácias, 45');
INSERT INTO clientes (cpf, nome, email, senha, celular, endereco) VALUES ('22222222222', 'Pedro Alves', 'pedro.alves@email.com', '123456', '51977770002', 'Av. Brasil, 780');
INSERT INTO clientes (cpf, nome, email, senha, celular, endereco) VALUES ('33333333333', 'Ana Lima', 'ana.lima@email.com', '123456', '51966660003', 'Rua das Palmeiras, 12');

-- ================================================
-- INGREDIENTES
-- ================================================
INSERT INTO ingredientes (id, descricao) VALUES (1, 'Disco de pizza');
INSERT INTO ingredientes (id, descricao) VALUES (2, 'Porcao de tomate');
INSERT INTO ingredientes (id, descricao) VALUES (3, 'Porcao de mussarela');
INSERT INTO ingredientes (id, descricao) VALUES (4, 'Porcao de presunto');
INSERT INTO ingredientes (id, descricao) VALUES (5, 'Porcao de calabresa');
INSERT INTO ingredientes (id, descricao) VALUES (6, 'Molho de tomate (200ml)');
INSERT INTO ingredientes (id, descricao) VALUES (7, 'Porcao de azeitona');
INSERT INTO ingredientes (id, descricao) VALUES (8, 'Porcao de oregano');
INSERT INTO ingredientes (id, descricao) VALUES (9, 'Porcao de cebola');


-- ================================================
-- RECEITAS
-- ================================================
INSERT INTO receitas (id, titulo) VALUES (1, 'Pizza calabresa');
INSERT INTO receitas (id, titulo) VALUES (2, 'Pizza queijo e presunto');
INSERT INTO receitas (id, titulo) VALUES (3, 'Pizza margherita');

-- Ingredientes da Pizza calabresa
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 6); -- Molho de tomate
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 3); -- Mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 5); -- Calabresa

-- Ingredientes da Pizza queijo e presunto
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 6); -- Molho de tomate
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 3); -- Mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 4); -- Presunto

-- Ingredientes da Pizza margherita
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 6); -- Molho de tomate
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 3); -- Mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 8); -- Oregano

-- ================================================
-- PRODUTOS
-- ================================================
INSERT INTO produtos (id, descricao, preco) VALUES (1, 'Pizza calabresa', 5500);
INSERT INTO produtos (id, descricao, preco) VALUES (2, 'Pizza queijo e presunto', 6000);
INSERT INTO produtos (id, descricao, preco) VALUES (3, 'Pizza margherita', 4000);
INSERT INTO produtos (id, descricao, preco) VALUES (4, 'Pizza portuguesa', 6500);
INSERT INTO produtos (id, descricao, preco) VALUES (5, 'Pizza frango com catupiry', 7000);
INSERT INTO produtos (id, descricao, preco) VALUES (6, 'Pizza quatro queijos', 7500);

-- Associacao produtos x receitas
INSERT INTO produto_receita (produto_id, receita_id) VALUES (1, 1);
INSERT INTO produto_receita (produto_id, receita_id) VALUES (2, 2);
INSERT INTO produto_receita (produto_id, receita_id) VALUES (3, 3);
INSERT INTO produto_receita (produto_id, receita_id) VALUES (4, 1);
INSERT INTO produto_receita (produto_id, receita_id) VALUES (5, 2);
INSERT INTO produto_receita (produto_id, receita_id) VALUES (6, 3);

-- ================================================
-- CARDAPIOS
-- ================================================
INSERT INTO cardapios (id, titulo) VALUES (1, 'Cardapio de Agosto');
INSERT INTO cardapios (id, titulo) VALUES (2, 'Cardapio de Setembro');
INSERT INTO cardapios (id, titulo) VALUES (3, 'Cardapio de Outubro');

-- Cardapio de Agosto (3 produtos)
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (1, 1);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (1, 2);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (1, 3);

-- Cardapio de Setembro (5 produtos)
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (2, 1);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (2, 3);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (2, 4);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (2, 5);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (2, 6);

-- Cardapio de Outubro (todos os produtos)
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (3, 1);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (3, 2);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (3, 3);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (3, 4);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (3, 5);
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (3, 6);

-- ================================================
-- CONFIG (cardapio ativo)
-- ================================================
INSERT INTO CONFIG (nome_cardapio, cardapio_ativo) VALUES ('cardapio_corrente', '3');

-- ================================================
-- PEDIDOS PRE-POPULADOS (para demonstracao)
-- ================================================

-- Pedido 1: Huguinho Pato - APROVADO (pronto para pagar ou cancelar - demo UC6/UC7)
INSERT INTO pedidos (id, cliente_cpf, data_criacao, data_hora_pagamento, data_entrega, status, valor, impostos, desconto, valor_cobrado, endereco_entrega)
VALUES (1, '9001', CURRENT_TIMESTAMP, NULL, NULL, 'APROVADO', 10500, 1050, 0, 11550, 'Rua das Flores, 100');
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (1, 1, 4, 1); -- 1x Pizza portuguesa
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (2, 1, 3, 1); -- 1x Pizza margherita

-- Pedido 2: Luizinho Pato - ENTREGUE (5 dias atras - demo UC8/UC9)
INSERT INTO pedidos (id, cliente_cpf, data_criacao, data_hora_pagamento, data_entrega, status, valor, impostos, desconto, valor_cobrado, endereco_entrega)
VALUES (2, '9002', DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP), 'ENTREGUE', 14000, 1400, 0, 15400, 'Av. Central, 200');
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (3, 2, 5, 2); -- 2x Pizza frango com catupiry

-- Pedido 3: Maria Souza - ENTREGUE (10 dias atras - demo UC8)
INSERT INTO pedidos (id, cliente_cpf, data_criacao, data_hora_pagamento, data_entrega, status, valor, impostos, desconto, valor_cobrado, endereco_entrega)
VALUES (3, '11111111111', DATEADD('DAY', -10, CURRENT_TIMESTAMP), DATEADD('DAY', -10, CURRENT_TIMESTAMP), DATEADD('DAY', -10, CURRENT_TIMESTAMP), 'ENTREGUE', 13000, 1300, 1300, 13000, 'Rua das Acácias, 45');
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (4, 3, 6, 1); -- 1x Pizza quatro queijos
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (5, 3, 1, 1); -- 1x Pizza calabresa

-- Pedido 4: Pedro Alves - PAGO (2 dias atras - demo status PAGO)
INSERT INTO pedidos (id, cliente_cpf, data_criacao, data_hora_pagamento, data_entrega, status, valor, impostos, desconto, valor_cobrado, endereco_entrega)
VALUES (4, '22222222222', DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP), NULL, 'PAGO', 4000, 400, 0, 4400, 'Av. Brasil, 780');
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (6, 4, 3, 1); -- 1x Pizza margherita

-- Pedido 5: Ana Lima - CANCELADO (3 dias atras - demo status CANCELADO)
INSERT INTO pedidos (id, cliente_cpf, data_criacao, data_hora_pagamento, data_entrega, status, valor, impostos, desconto, valor_cobrado, endereco_entrega)
VALUES (5, '33333333333', DATEADD('DAY', -3, CURRENT_TIMESTAMP), NULL, NULL, 'CANCELADO', 13000, 1300, 0, 14300, 'Rua das Palmeiras, 12');
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (7, 5, 4, 2); -- 2x Pizza portuguesa

-- Pedido 6: Huguinho Pato - ENTREGUE (20 dias atras - demo UC9 meus pedidos)
INSERT INTO pedidos (id, cliente_cpf, data_criacao, data_hora_pagamento, data_entrega, status, valor, impostos, desconto, valor_cobrado, endereco_entrega)
VALUES (6, '9001', DATEADD('DAY', -20, CURRENT_TIMESTAMP), DATEADD('DAY', -20, CURRENT_TIMESTAMP), DATEADD('DAY', -20, CURRENT_TIMESTAMP), 'ENTREGUE', 22500, 2250, 2250, 22500, 'Rua das Flores, 100');
INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade) VALUES (8, 6, 6, 3); -- 3x Pizza quatro queijos

-- Garante que os proximos IDs gerados pelo auto_increment nao colidem com os inseridos acima
ALTER TABLE pedidos ALTER COLUMN id RESTART WITH 100;
ALTER TABLE itens_pedido ALTER COLUMN id RESTART WITH 100;
