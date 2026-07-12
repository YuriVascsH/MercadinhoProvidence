package br.com.mercadinhoprovidence.balance;

public class Weight {

    private BigDecimal valor;

    private boolean estabilizado;

    public Peso(BigDecimal valor, boolean estabilizado) {
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