package br.com.mercadinhoprovidence.model;

import java.math.BigDecimal;
import java.util.Date;

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
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private Date validade;
    private BigDecimal quantOuPesoEmEstoque;
    private BigDecimal desconto;
    private Boolean active;

}
