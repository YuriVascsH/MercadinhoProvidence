CREATE TABLE item_venda (
    id_item_venda INT AUTO_INCREMENT PRIMARY KEY,
    id_venda INT NOT NULL,
    id_produto INT NOT NULL,
    numero_item INT NOT NULL, -- Sequência no cupom: 1, 2, 3...
    quantidade DECIMAL(10, 3) NOT NULL, -- Suporta kg (ex: 1.250) e unidades
    preco_unitario DECIMAL(10, 2) NOT NULL,
    desconto_item DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    valor_total_item DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_item_venda_venda FOREIGN KEY (id_venda) 
        REFERENCES venda (id_venda) ON DELETE CASCADE,
        
    CONSTRAINT fk_item_venda_produto FOREIGN KEY (id_produto) 
        REFERENCES produto (id_produto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;