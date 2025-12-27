package br.com.mercadinhoprovidence.dto.funcionario;

import br.com.mercadinhoprovidence.model.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioUpdateDto {

    private Integer idFuncionario;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private Cargo cargo;
    private BigDecimal salario;
    private Boolean ativo;

}
