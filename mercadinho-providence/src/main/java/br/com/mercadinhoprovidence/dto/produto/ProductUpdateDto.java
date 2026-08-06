package br.com.mercadinhoprovidence.dto.produto;

import java.math.BigDecimal;
import java.util.Date;

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
    private String codigoBarras;
    private String descricao;
    private Category categoria;
    private Boolean controlaEstoque;
    private BigDecimal precoVenda;
    private Date validade;
    private BigDecimal quantOuPesoEmEstoque;

}
