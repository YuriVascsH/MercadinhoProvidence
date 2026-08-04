package br.com.mercadinhoprovidence.balance;

import java.math.BigDecimal;

public class Weight {

    private BigDecimal valor;

    private boolean estabilizado;

    public Weight(BigDecimal valor, boolean estabilizado) {
        this.valor = valor;
        this.estabilizado = estabilizado;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public boolean isEstabilizado() {
        return estabilizado;
    }

}