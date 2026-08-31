CREATE DATABASE mercadinho_providence;
USE mercadinho_providence;
# DROP DATABASE mercadinho_providence;


CREATE TABLE Funcionario (
    id_funcionario     INT AUTO_INCREMENT PRIMARY KEY,
    codigo_verificador INT		 		 NOT NULL UNIQUE,
    cpf 			  VARCHAR(11)   	 NOT NULL UNIQUE,
    nome 			  VARCHAR(50)   	 NOT NULL,
    data_nascimento    DATE 			 DEFAULT '0001-01-01 00:00:00',
    telefone 		  VARCHAR(11)   	 NOT NULL,
    email 			  VARCHAR(100)  	 DEFAULT '',
    endereco 		  VARCHAR(200)  	 DEFAULT '',
    data_admissao 	  DATE 	  		     DEFAULT(CURRENT_DATE()),
    cargo 			  ENUM('Gerente', 'Operador')   NOT NULL,
    salario 		  DECIMAL(10,2) 	 NOT NULL,
    senha 			  VARCHAR(50)   	 NOT NULL,
    ativo 			  BOOLEAN			 DEFAULT TRUE
) AUTO_INCREMENT = 1000;
#--------------------------------------------------------------------------------------
CREATE TABLE Gerente ( # Model finalizado!!!
    id_gerente 	  INT AUTO_INCREMENT PRIMARY KEY,
    id_funcionario INT NOT NULL,
    FOREIGN KEY (id_funcionario) REFERENCES Funcionario(id_funcionario) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE Operador ( # Model finalizado!!!
    id_operador	  INT AUTO_INCREMENT PRIMARY KEY,
    id_funcionario INT 		  NOT NULL,
    ultima_venda  DATETIME,
    FOREIGN KEY (id_funcionario) REFERENCES Funcionario(id_funcionario) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE Estoque ( # Model finalizado!!!
    id_estoque 			   INT AUTO_INCREMENT PRIMARY KEY
);
#--------------------------------------------------------------------------------------
CREATE TABLE Produto ( # Model finalizado!!!
    id_produto 	   		INT AUTO_INCREMENT PRIMARY KEY,
    id_estoque 	   		INT 		  DEFAULT '1',
    codigo_de_barras 	VARCHAR(100)  NOT NULL,
    nome 		   		VARCHAR(75)   NOT NULL,
    ativo 		   		BOOLEAN 	  DEFAULT TRUE,
    controla_estoque	BOOLEAN		  DEFAULT TRUE,
    descricao 	   		VARCHAR(100)  DEFAULT 'Não informado',
    preco_custo	   		DECIMAL(10,2) NULL,
    preco_venda	   		DECIMAL(10,2) NOT NULL,
    validade			DATETIME 	  NULL,
    quant_ou_peso_em_estoque DECIMAL(10,2) NOT NULL,
    desconto			DECIMAL(10,2) NULL,
    categoria ENUM('UN', 'KG', 'HORTI', 'AVULSO') NOT NULL DEFAULT 'UN',
    FOREIGN KEY (id_estoque) REFERENCES Estoque(id_estoque) ON DELETE CASCADE
);

#--------------------------------------------------------------------------------------
CREATE TABLE TotalProdutosCadastrados (
    id INT PRIMARY KEY DEFAULT 1,
    total INT DEFAULT 0
);
INSERT INTO TotalProdutosCadastrados (total) VALUES (0);
# -- Inicializa com 0
#--------------------------------------------------------------------------------------
CREATE TABLE VENDAS ( 
    id_venda            INT AUTO_INCREMENT PRIMARY KEY,
    data_hora           DATETIME      DEFAULT CURRENT_TIMESTAMP,
    valor_subtotal      DECIMAL(10,2) NOT NULL,
    valor_desconto      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valor_total         DECIMAL(10,2) NOT NULL,
    id_funcionario      INT           NOT NULL,
    FOREIGN KEY (id_funcionario) REFERENCES Funcionario(id_funcionario) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE ITENS_VENDA ( 
    id_item_venda         INT AUTO_INCREMENT PRIMARY KEY, 
    id_venda             INT NOT NULL, 
    id_produto           INT NOT NULL,
    quantidade_ou_peso  DECIMAL(10,2) NOT NULL,
    preco_unitario_venda  DECIMAL(10,2) NOT NULL, 
    total_item           DECIMAL(10,2) NOT NULL, 
    FOREIGN KEY (id_venda) REFERENCES VENDAS(id_venda) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE PAGAMENTOS (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    troco       DECIMAL(10,2) NOT NULL,
    valor_pago   DECIMAL(10,2) NOT NULL,
    forma       ENUM('Dinheiro', 'Pix', 'Débito', 'Crédito', 'Vale Alimentação', 'Vale Refeição')   DEFAULT 'Dinheiro',
    id_venda     INT           NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES VENDAS(id_venda) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE TotalVendasDiarias (
    data DATE PRIMARY KEY,
    total DECIMAL(10,2) DEFAULT 0.00
);
#--------------------------------------------------------------------------------------
#------------------------------------TRIGGERS OPERADOR---------------------------------
DELIMITER $$

CREATE TRIGGER atualizar_ultima_venda_operador
AFTER INSERT ON VENDAS
FOR EACH ROW
BEGIN
    -- Verifica se o funcionário que fez o pedido é um operador
    IF EXISTS (
        SELECT 1
        FROM Operador
        WHERE id_funcionario = NEW.id_funcionario
    ) THEN
        -- Atualiza a data da última venda do operador
        UPDATE Operador
        SET ultima_venda = NOW()
        WHERE id_funcionario = NEW.id_funcionario;
    END IF;
END$$

DELIMITER ;
#-------------------------------------TRIGGERS ESTOQUE----------------------------------
DELIMITER $$
CREATE TRIGGER atualizar_estoque_venda
AFTER INSERT ON ITENS_VENDA
FOR EACH ROW
BEGIN
    UPDATE Produto
    SET quant_ou_peso_em_estoque = quant_ou_peso_em_estoque - NEW.quantidade_ou_peso
    WHERE id_produto = NEW.id_produto;
END$$
DELIMITER ;
#-------------------------------------TRIGGERS PRODUTO----------------------------------
DELIMITER $$

CREATE TRIGGER after_insert_produto
AFTER INSERT ON Produto
FOR EACH ROW
BEGIN
    UPDATE TotalProdutosCadastrados
    SET total = total + 1
    WHERE id = 1;
END$$

DELIMITER ;
#--------------------------------------------------------------------------------------
DELIMITER $$

CREATE TRIGGER after_delete_produto
AFTER DELETE ON Produto
FOR EACH ROW
BEGIN
    UPDATE TotalProdutosCadastrados
    SET total = total - 1
    WHERE id = 1;
END$$

DELIMITER ;
#--------------------------------------------------------------------------------------
DELIMITER $$

CREATE TRIGGER after_update_produto
AFTER UPDATE ON Produto
FOR EACH ROW
BEGIN
    IF OLD.ativo = TRUE AND NEW.ativo = FALSE THEN
        UPDATE TotalProdutosCadastrados
        SET total = total - 1
        WHERE id = 1;
    ELSEIF OLD.ativo = FALSE AND NEW.ativo = TRUE THEN
        UPDATE TotalProdutosCadastrados
        SET total = total + 1
        WHERE id = 1;
    END IF;
END$$
#--------------------------------------------------------------------------------------
#--------------------------------TRIGGERS TOTAL VENDAS/DIA------------------------------

DELIMITER $$

CREATE TRIGGER atualizar_total_vendas_diarias
AFTER INSERT ON VENDAS
FOR EACH ROW
BEGIN
    -- Verifica se já existe um registro para o dia atual
    IF EXISTS (
        SELECT 1 FROM TotalVendasDiarias WHERE data = CURDATE()
    ) THEN
        -- Atualiza somando o valor do novo pedido
        UPDATE TotalVendasDiarias
        SET total = total + NEW.valor_total
        WHERE data = CURDATE();
    ELSE
        -- Cria o registro do dia com o valor do primeiro pedido
        INSERT INTO TotalVendasDiarias (data, total)
        VALUES (CURDATE(), NEW.valor_total);
    END IF;
END$$

DELIMITER ;

INSERT INTO Estoque (id_estoque) VALUES (1);

INSERT INTO Funcionario (codigo_verificador, cpf, nome, data_nascimento, telefone, email, endereco, cargo, salario, senha, ativo)
VALUES (12345, '11122233344', 'Amanda Gerente', '1985-05-20 00:00:00', '81987654321', 'amanda.g@mercadinho.com', 'Rua Principal, 100, Centro', 'Gerente', 4500.00, '123456', TRUE);
INSERT INTO Funcionario (codigo_verificador, cpf, nome, data_nascimento, telefone, email, endereco, cargo, salario, senha, ativo)
VALUES (54321, '22233344455', 'Bruno Operador', '1990-10-15 00:00:00', '81998765432', 'bruno.o@mercadinho.com', 'Av. Secundária, 200, Bairro Novo', 'Operador', 2200.00, '123456', TRUE);
INSERT INTO Funcionario (codigo_verificador, cpf, nome, data_nascimento, telefone, email, endereco, cargo, salario, senha, ativo)
VALUES (98765, '33344455566', 'Carla Operadora', '1995-03-25 00:00:00', '81976543210', 'carla.o@mercadinho.com', 'Rua dos Mercados, 30, Bairro Antigo', 'Operador', 2100.00, '123456', TRUE);

INSERT INTO Gerente (id_funcionario) VALUES (1000);

INSERT INTO Operador (id_funcionario, ultima_venda) VALUES (1001, '2025-10-20 10:30:00');
INSERT INTO Operador (id_funcionario, ultima_venda) VALUES (1002, '2025-10-20 11:45:00');

INSERT INTO Produto (id_estoque, codigo_de_barras, nome, descricao, preco_custo, preco_venda, validade, quant_ou_peso_em_estoque, desconto, categoria)
VALUES (1, '7891000000010', 'Arroz Agulhinha 5kg', 'Arroz Tipo 1', 18.00, 25.00, '2026-12-31 00:00:00', 50.00, 0.00, 'UN');
INSERT INTO Produto (id_estoque, codigo_de_barras, nome, descricao, preco_custo, preco_venda, validade, quant_ou_peso_em_estoque, desconto, categoria)
VALUES (1, '7891000000027', 'Café Tradicional 500g', 'Torrado e Moído', 9.50, 14.99, '2026-06-30 00:00:00', 80.00, 0.00, 'AVULSO');
INSERT INTO Produto (id_estoque, codigo_de_barras, nome, descricao, preco_custo, preco_venda, validade, quant_ou_peso_em_estoque, desconto, categoria)
VALUES (1, '9991000000034', 'Maçã Fuji', 'Fruta Fresca por KG', 3.50, 6.99, '2025-10-25 00:00:00', 30.00, 0.50, 'HORTI');
INSERT INTO Produto (id_estoque, codigo_de_barras, nome, descricao, preco_custo, preco_venda, validade, quant_ou_peso_em_estoque, desconto, categoria)
VALUES (1, '7891000000041', 'Sabonete Glicerina', 'Unidade 90g', 2.00, 3.50, '2027-01-01 00:00:00', 120.00, 0.00, 'UN');
INSERT INTO Produto (id_estoque, codigo_de_barras, nome, descricao, preco_custo, preco_venda, validade, quant_ou_peso_em_estoque, desconto, categoria)
VALUES (1, '7891000000058', 'Refrigerante Cola 2L', 'Garrafa PET', 6.00, 10.00, '2026-03-01 00:00:00', 60.00, 1.00, 'UN');
UPDATE TotalProdutosCadastrados SET total = 5 WHERE id = 1;

INSERT INTO VENDAS (data_hora, valor_subtotal, valor_desconto, valor_total, id_funcionario)
VALUES ('2025-10-20 10:30:00', 53.495, 1.00, 52.495, 1001);
INSERT INTO VENDAS (data_hora, valor_subtotal, valor_desconto, valor_total, id_funcionario)
VALUES ('2025-10-20 11:45:00', 48.47, 0.00, 48.47, 1002);

INSERT INTO ITENS_VENDA (id_venda, id_produto, quantidade_ou_peso, preco_unitario_venda, total_item)
VALUES (1, 1, 2.00, 25.00, 50.00);
INSERT INTO ITENS_VENDA (id_venda, id_produto, quantidade_ou_peso, preco_unitario_venda, total_item)
VALUES (1, 3, 0.50, 6.99, 3.495);
UPDATE VENDAS SET valor_subtotal = (50.00 + 3.495), valor_desconto = 1.00, valor_total = (50.00 + 3.495 - 1.00) WHERE id_venda = 1;
INSERT INTO ITENS_VENDA (id_venda, id_produto, quantidade_ou_peso, preco_unitario_venda, total_item)
VALUES (2, 2, 3.00, 14.99, 44.97);
INSERT INTO ITENS_VENDA (id_venda, id_produto, quantidade_ou_peso, preco_unitario_venda, total_item)
VALUES (2, 4, 1.00, 3.50, 3.50);
UPDATE VENDAS SET valor_subtotal = (44.97 + 3.50), valor_desconto = 0.00, valor_total = (44.97 + 3.50) WHERE id_venda = 2;
INSERT INTO PAGAMENTOS (troco, valor_pago, forma, id_venda)
VALUES (60.00 - 52.495, 60.00, 'Dinheiro', 1);
INSERT INTO PAGAMENTOS (troco, valor_pago, forma, id_venda)
VALUES (0.00, 48.47, 'Crédito', 2);
INSERT INTO TotalVendasDiarias (data, total)
VALUES ('2025-10-20', 100.97);