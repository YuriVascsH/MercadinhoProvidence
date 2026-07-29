package br.com.mercadinhoprovidence.dto.produto;

import java.math.BigDecimal;

import br.com.mercadinhoprovidence.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {
    private String nome;
    private String codigoBarras;
    private String descricao;
    private Category categoria;
    private Boolean controlaEstoque;
    private BigDecimal precoUnitario;
    private BigDecimal precoPorKg;
    private BigDecimal quantidadeInicial;
}
