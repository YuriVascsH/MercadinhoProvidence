CREATE TABLE pagamento_venda (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    id_venda INT NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL, -- DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX
    valor_pago DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_pagamento_venda FOREIGN KEY (id_venda) 
        REFERENCES venda (id_venda) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;