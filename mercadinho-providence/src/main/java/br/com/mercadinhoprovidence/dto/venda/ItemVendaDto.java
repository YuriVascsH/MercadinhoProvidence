package br.com.mercadinhoprovidence.dto.venda;

/**
 * Representa um item dentro de uma venda/cupom fiscal.
 * Extraído da classe interna que existia em TelaInicialView.
 */
public class ItemVendaDto {

    private final String codigo;
    private final String descricao;
    private final double quantidade;
    private final String unidade; // "UN", "KG", "G"
    private final double valorUnitario;

    public ItemVendaDto(String codigo, String descricao, double quantidade, String unidade, double valorUnitario) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.valorUnitario = valorUnitario;
    }

    public double getValorTotal() {
        return quantidade * valorUnitario;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }
}