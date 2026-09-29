CREATE TABLE venda (
    id_venda INT AUTO_INCREMENT PRIMARY KEY,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_funcinoario INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT "EM_ANDAMENTO",
    valor_subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valor_desconto DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valor_pago DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    troco DECIMAL(10,2) NOT NULL DEFAULT 0.00

    CONSTRAINT fk_venda_funcinario FOREIGN KEY (id_funcionario) REFERENCES funcionario (id_funcionario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4