USE mercadinhoprovidence;

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

DESCRIBE funcionario;

INSERT INTO funcionario (
    id_funcionario,
    codigo_verificador, 
    cpf, 
    nome, 
    data_nascimento, 
    telefone, 
    email, 
    endereco, 
    cargo, 
    salario, 
    senha, 
    ativo
) VALUES (
    1023,                             -- id_funcionario (Forçado para o teste)
    9988,                             -- codigo_verificador (O código impresso)
    '123.456.789-00',                 -- cpf
    'João Silva',                     -- nome
    '1995-05-15',                     -- data_nascimento (Formato YYYY-MM-DD)
    '(81) 99999-9999',                -- telefone
    'joao.caixa@providence.com',      -- email
    'Rua do Mercado, 123 - Centro',   -- endereco
    'OPERADOR',                       -- cargo
    1412.00,                          -- salario
    '123456',                         -- senha (Depois você pode usar hash BCrypt aqui!)
    TRUE                              -- ativo
);

SELECT * FROM funcionario;