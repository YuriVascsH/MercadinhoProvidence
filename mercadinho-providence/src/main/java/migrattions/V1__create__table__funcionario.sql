CREATE TABLE IF NOT EXISTS funcionario (
    id_funcionario INT PRIMARY KEY AUTO_INCREMENT,
    codigo_verificador INT UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    nome VARCHAR(150) NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(150) UNIQUE NOT NULL,
    endereco VARCHAR(255),
    data_admissao DATE NOT NULL DEFAULT (CURRENT_DATE()),
    cargo VARCHAR(50) NOT NULL,
    salario DECIMAL(10, 2) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ultima_venda DATETIME,
    -- Dados autorais
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
