package br.com.mercadinhoprovidence.dto.produto;

import java.math.BigDecimal;

import br.com.mercadinhoprovidence.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

    private Integer id;
    private String nome;
    private Category categoria;
    private String descricao;
    private BigDecimal precoUnitario;
    private BigDecimal precoPorKg;
    private Boolean controlaEstoque;

}
