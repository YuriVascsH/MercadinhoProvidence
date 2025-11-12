package br.com.mercadinhoprovidence.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.mercadinhoprovidence.model.enums.Forma;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Venda {

    private Integer idVenda;
    private LocalDateTime dataHora;
    private Double valorSubtotal;
    private Double valorDesconto;
    private Double valorTotal;
    private Integer idFuncionario;
    private Double valorTotalManual;

    @Builder.Default
    private List<ItemVenda> itensVenda = new ArrayList<ItemVenda>();
    private Double troco;
    private Double valorPago;
    private Forma forma;

    public Venda(int idVenda, LocalDateTime dataHora, double valorSubtotal, double valorDesconto, double valorTotal, int idFuncionario, double valorTotalManual) {
    }
}
