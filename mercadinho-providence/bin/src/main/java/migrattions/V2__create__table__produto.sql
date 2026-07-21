CREATE TABLE produto (
    id_produto INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(75) NOT NULL,
    codigo_barras VARCHAR(105) UNIQUE NOT NULL,
    descricao VARCHAR(150) NOT NULL,

    categoria ENUM('UNIDADE', 'PESO') NOT NULL,

    controla_estoque BOOLEAN NOT NULL DEFAULT TRUE,

    preco_unitario DECIMAL(10,2),
    preco_por_kg   DECIMAL(10,2),

    CHECK (
        (categoria = 'UNIDADE' AND preco_unitario IS NOT NULL)
        OR
        (categoria = 'PESO' AND preco_por_kg IS NOT NULL)
    )
);

CREATE TABLE estoque (
    id_estoque INT AUTO_INCREMENT PRIMARY KEY,
    id_produto INT NOT NULL UNIQUE,
    quantidade DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (id_produto)
        REFERENCES produto(id_produto)
        ON DELETE CASCADE
);


CREATE TABLE movimentacaoEstoque (
    id_movimentacao INT AUTO_INCREMENT PRIMARY KEY,

    id_produto INT NOT NULL,

    tipo_movimentacao ENUM(
        'ENTRADA_COMPRA',
        'SAIDA_VENDA',
        'AJUSTE_PERDA',
        'AJUSTE_GANHO'
    ) NOT NULL,

    quantidade DECIMAL(10,2) NOT NULL,
    data_movimentacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacao VARCHAR(255),

    FOREIGN KEY (id_produto)
        REFERENCES produto(id_produto)
);
