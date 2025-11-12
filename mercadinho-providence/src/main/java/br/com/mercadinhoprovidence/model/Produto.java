package br.com.mercadinhoprovidence.model;

import java.time.LocalDate;

import br.com.mercadinhoprovidence.model.enums.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    private Integer idProduto;
    private Integer idEstoque;
    private String codigoDeBarras;
    private String nome;
    private Boolean ativo;
    private String descricao;
    private Double precoCusto;
    private Double precoVenda;
    private LocalDate validade;
    private Integer quantidadeOuPesoEmEstoque;
    private Double desconto;
    private Categoria categoria;

}
