package br.com.mercadinhoprovidence.dto.Funcionario;

import br.com.mercadinhoprovidence.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponseDto {

    private String nome;
    private Integer codigoVerificador;

    public FuncionarioResponseDto(Funcionario funcionario) {
        this.nome = funcionario.getNome();
        this.codigoVerificador = funcionario.getCodigoVerificador();
    }

}
