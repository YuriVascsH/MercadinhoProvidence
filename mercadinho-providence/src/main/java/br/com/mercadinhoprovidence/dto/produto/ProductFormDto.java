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
public class ProductFormDto {
    
    private Integer id;
    private String nome;
    private String codigoBarras;
    private String descricao;
    private Category categoria;
    private boolean controlaEstoque;
    private BigDecimal precoVenda;
}
