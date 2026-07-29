package br.com.mercadinhoprovidence.model;

import java.math.BigDecimal;

import br.com.mercadinhoprovidence.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Integer idProduto;
    private String nome;
    private String codigoDeBarras;
    private String descricao;
    private Category categoria;
    private Boolean controlaEstoque;
    private BigDecimal precoUnitario;
    private BigDecimal precoPorKg;
    private Boolean active;

}
