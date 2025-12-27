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
public class ProdutoFormDto {
    
    private Integer id;
    public String nome;
    public String codigoBarras;
    public String descricao;
    public Category categoria;
    public boolean controlaEstoque;
    public BigDecimal precoUnitario;
    public BigDecimal precoPorKg;
}
