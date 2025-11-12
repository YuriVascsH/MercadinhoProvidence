package br.com.mercadinhoprovidence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder 
@NoArgsConstructor 
@AllArgsConstructor
public class ItemVenda {

    private Integer idItemVenda;
    private Integer idVenda;
    private Double quantidadeOuPeso;
    private Double precoUnitarioVenda;
    private Double totalItem;

    private Produto produto;

}
