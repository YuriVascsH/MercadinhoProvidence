package br.com.mercadinhoprovidence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder 
@NoArgsConstructor 
@AllArgsConstructor
public class LoteEstoque {

    private Integer idLote;
    private Integer idProduto;
    private BigDecimal precoCusto;
    private LocalDate validade;
    private Double quantidadeAtual;
    private LocalDateTime dataEntrada;
    private Boolean ativo;

}
