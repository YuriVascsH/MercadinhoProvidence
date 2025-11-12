CREATE DATABASE mercadinho_providence;
USE mercadinho_providence;
DROP DATABASE mercadinho_providence;


CREATE TABLE Funcionario (
    idFuncionario     INT AUTO_INCREMENT PRIMARY KEY,
    codigoVerificador INT		 		 NOT NULL UNIQUE,
    cpf 			  VARCHAR(11)   	 NOT NULL UNIQUE,
    nome 			  VARCHAR(50)   	 NOT NULL,
    dataNascimento    DATE 			 DEFAULT '0001-01-01 00:00:00',
    telefone 		  VARCHAR(11)   	 NOT NULL,
    email 			  VARCHAR(100)  	 DEFAULT '',
    endereco 		  VARCHAR(200)  	 DEFAULT '',
    dataAdmissao 	  DATE 	  		     DEFAULT(CURRENT_DATE()),
    cargo 			  ENUM('Gerente', 'Operador')   NOT NULL,
    salario 		  DECIMAL(10,2) 	 NOT NULL,
    senha 			  VARCHAR(50)   	 NOT NULL,
    ativo 			  BOOLEAN			 DEFAULT TRUE
) AUTO_INCREMENT = 1000;
#--------------------------------------------------------------------------------------
CREATE TABLE Gerente ( # Model finalizado!!!
    idGerente 	  INT AUTO_INCREMENT PRIMARY KEY,
    idFuncionario INT NOT NULL,
    FOREIGN KEY (idFuncionario) REFERENCES Funcionario(idFuncionario) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE Operador ( # Model finalizado!!!
    idOperador	  INT AUTO_INCREMENT PRIMARY KEY,
    idFuncionario INT 		  NOT NULL,
    ultimaVenda   DATETIME,
    FOREIGN KEY (idFuncionario) REFERENCES Funcionario(idFuncionario) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE Estoque ( # Model finalizado!!!
    idEstoque 			   INT AUTO_INCREMENT PRIMARY KEY
);
#--------------------------------------------------------------------------------------
CREATE TABLE Produto ( # Model finalizado!!!
    idProduto 	   		INT AUTO_INCREMENT PRIMARY KEY,
    idEstoque 	   		INT 		  DEFAULT '1',
    codigoDeBarras 		VARCHAR(100)  NOT NULL,
    nome 		   		VARCHAR(75)   NOT NULL,
    ativo 		   		BOOLEAN 	  DEFAULT TRUE,
    descricao 	   		VARCHAR(100)  DEFAULT 'Não informado',
    precoCusto 	   		DECIMAL(10,2) NULL,
    precoVenda 	   		DECIMAL(10,2) NOT NULL,
    validade 			DATETIME 	  NULL,
    quantidadeOuPesoEmEstoque DECIMAL(10,2) NOT NULL,
    desconto			DECIMAL(10,2) NULL,
    categoria ENUM('GERAL','AVULSOS','HORTI') NOT NULL DEFAULT 'GERAL',
    FOREIGN KEY (idEstoque) REFERENCES Estoque(idEstoque) ON DELETE CASCADE
);

#--------------------------------------------------------------------------------------
CREATE TABLE TotalProdutosCadastrados (
    id INT PRIMARY KEY DEFAULT 1,
    total INT DEFAULT 0
);
-- Inicializa com 0
INSERT INTO TotalProdutosCadastrados (id, total) VALUES (1, 0);
#--------------------------------------------------------------------------------------
CREATE TABLE VENDAS ( 
    idVenda            INT AUTO_INCREMENT PRIMARY KEY,
    dataHora           DATETIME      DEFAULT CURRENT_TIMESTAMP,
    valorSubtotal      DECIMAL(10,2) NOT NULL,
    valorDesconto      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valorTotal         DECIMAL(10,2) NOT NULL,
    idFuncionario      INT           NOT NULL,
    FOREIGN KEY (idFuncionario) REFERENCES Funcionario(idFuncionario) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE ITENS_VENDA ( 
    idItemVenda         INT AUTO_INCREMENT PRIMARY KEY, 
    idVenda             INT NOT NULL, 
    idProduto           INT NOT NULL,
    quantidade_ou_peso  DECIMAL(10,2) NOT NULL,
    precoUnitarioVenda  DECIMAL(10,2) NOT NULL, 
    totalItem           DECIMAL(10,2) NOT NULL, 
    FOREIGN KEY (idVenda) REFERENCES VENDAS(idVenda) ON DELETE CASCADE,
    FOREIGN KEY (idProduto) REFERENCES Produto(idProduto) ON DELETE CASCADE
);
#--------------------------------------------------------------------------------------
CREATE TABLE PAGAMENTOS (
    idPagamento INT AUTO_INCREMENT PRIMARY KEY,
    troco       DECIMAL(10,2) NOT NULL,
    valorPago   DECIMAL(10,2) NOT NULL,
    forma       ENUM('Dinheiro', 'Pix', 'Débito', 'Crédito', 'Vale Alimentação', 'Vale Refeição')   DEFAULT 'Dinheiro',
    idVenda     INT           NOT NULL,
    FOREIGN KEY (idVenda) REFERENCES VENDAS(idVenda) ON DELETE CASCADE
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
        WHERE idFuncionario = NEW.idFuncionario
    ) THEN
        -- Atualiza a data da última venda do operador
        UPDATE Operador
        SET ultimaVenda = NOW()
        WHERE idFuncionario = NEW.idFuncionario;
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
    SET quantidadeEmEstoque = quantidadeEmEstoque - NEW.quantidade_ou_peso
    WHERE idProduto = NEW.idProduto;
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
        SET total = total + NEW.valorTotal
        WHERE data = CURDATE();
    ELSE
        -- Cria o registro do dia com o valor do primeiro pedido
        INSERT INTO TotalVendasDiarias (data, total)
        VALUES (CURDATE(), NEW.valorTotal);
    END IF;
END$$

DELIMITER ;

INSERT INTO Estoque (idEstoque) VALUES (1);
INSERT INTO Funcionario (codigoVerificador, cpf, nome, dataNascimento, telefone, email, endereco, cargo, salario, senha, ativo)
VALUES (12345, '11122233344', 'Amanda Gerente', '1985-05-20 00:00:00', '81987654321', 'amanda.g@mercadinho.com', 'Rua Principal, 100, Centro', 'Gerente', 4500.00, '123456', TRUE);
INSERT INTO Funcionario (codigoVerificador, cpf, nome, dataNascimento, telefone, email, endereco, cargo, salario, senha, ativo)
VALUES (54321, '22233344455', 'Bruno Operador', '1990-10-15 00:00:00', '81998765432', 'bruno.o@mercadinho.com', 'Av. Secundária, 200, Bairro Novo', 'Operador', 2200.00, '123456', TRUE);
INSERT INTO Funcionario (codigoVerificador, cpf, nome, dataNascimento, telefone, email, endereco, cargo, salario, senha, ativo)
VALUES (98765, '33344455566', 'Carla Operadora', '1995-03-25 00:00:00', '81976543210', 'carla.o@mercadinho.com', 'Rua dos Mercados, 30, Bairro Antigo', 'Operador', 2100.00, '123456', TRUE);
INSERT INTO Gerente (idFuncionario) VALUES (1000);
INSERT INTO Operador (idFuncionario, ultimaVenda) VALUES (1001, '2025-10-20 10:30:00');
INSERT INTO Operador (idFuncionario, ultimaVenda) VALUES (1002, '2025-10-20 11:45:00');
INSERT INTO Produto (idEstoque, codigoDeBarras, nome, descricao, precoCusto, precoVenda, validade, quantidadeOuPesoEmEstoque, desconto, categoria)
VALUES (1, '7891000000010', 'Arroz Agulhinha 5kg', 'Arroz Tipo 1', 18.00, 25.00, '2026-12-31 00:00:00', 50.00, 0.00, 'GERAL');
INSERT INTO Produto (idEstoque, codigoDeBarras, nome, descricao, precoCusto, precoVenda, validade, quantidadeOuPesoEmEstoque, desconto, categoria)
VALUES (1, '7891000000027', 'Café Tradicional 500g', 'Torrado e Moído', 9.50, 14.99, '2026-06-30 00:00:00', 80.00, 0.00, 'AVULSOS');
INSERT INTO Produto (idEstoque, codigoDeBarras, nome, descricao, precoCusto, precoVenda, validade, quantidadeOuPesoEmEstoque, desconto, categoria)
VALUES (1, '9991000000034', 'Maçã Fuji', 'Fruta Fresca por KG', 3.50, 6.99, '2025-10-25 00:00:00', 30.00, 0.50, 'HORTI');
INSERT INTO Produto (idEstoque, codigoDeBarras, nome, descricao, precoCusto, precoVenda, validade, quantidadeOuPesoEmEstoque, desconto, categoria)
VALUES (1, '7891000000041', 'Sabonete Glicerina', 'Unidade 90g', 2.00, 3.50, '2027-01-01 00:00:00', 120.00, 0.00, 'GERAL');
INSERT INTO Produto (idEstoque, codigoDeBarras, nome, descricao, precoCusto, precoVenda, validade, quantidadeOuPesoEmEstoque, desconto, categoria)
VALUES (1, '7891000000058', 'Refrigerante Cola 2L', 'Garrafa PET', 6.00, 10.00, '2026-03-01 00:00:00', 60.00, 1.00, 'GERAL');
UPDATE TotalProdutosCadastrados SET total = 5 WHERE id = 1;
INSERT INTO VENDAS (dataHora, valorSubtotal, valorDesconto, valorTotal, idFuncionario)
VALUES ('2025-10-20 10:30:00', 53.495, 1.00, 52.495, 1001);
INSERT INTO VENDAS (dataHora, valorSubtotal, valorDesconto, valorTotal, idFuncionario)
VALUES ('2025-10-20 11:45:00', 48.47, 0.00, 48.47, 1002);
INSERT INTO ITENS_VENDA (idVenda, idProduto, quantidade_ou_peso, precoUnitarioVenda, totalItem)
VALUES (1, 1, 2.00, 25.00, 50.00);
INSERT INTO ITENS_VENDA (idVenda, idProduto, quantidade_ou_peso, precoUnitarioVenda, totalItem)
VALUES (1, 3, 0.50, 6.99, 3.495);
UPDATE VENDAS SET valorSubtotal = (50.00 + 3.495), valorDesconto = 1.00, valorTotal = (50.00 + 3.495 - 1.00) WHERE idVenda = 1;
INSERT INTO ITENS_VENDA (idVenda, idProduto, quantidade_ou_peso, precoUnitarioVenda, totalItem)
VALUES (2, 2, 3.00, 14.99, 44.97);
INSERT INTO ITENS_VENDA (idVenda, idProduto, quantidade_ou_peso, precoUnitarioVenda, totalItem)
VALUES (2, 4, 1.00, 3.50, 3.50);
UPDATE VENDAS SET valorSubtotal = (44.97 + 3.50), valorDesconto = 0.00, valorTotal = (44.97 + 3.50) WHERE idVenda = 2;
INSERT INTO PAGAMENTOS (troco, valorPago, forma, idVenda)
VALUES (60.00 - 52.495, 60.00, 'Dinheiro', 1);
INSERT INTO PAGAMENTOS (troco, valorPago, forma, idVenda)
VALUES (0.00, 48.47, 'Crédito', 2);
INSERT INTO TotalVendasDiarias (data, total)
VALUES ('2025-10-20', 100.97);