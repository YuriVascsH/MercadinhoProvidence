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
public class ProductTableDto {
    
    private Integer idProduto;
    private String nome;
    private String codigoDeBarras;
    private Category categoria;
    private Boolean controlaEsoque;
    private BigDecimal precoUnitario;
    private BigDecimal precoPorKg;
    

}
