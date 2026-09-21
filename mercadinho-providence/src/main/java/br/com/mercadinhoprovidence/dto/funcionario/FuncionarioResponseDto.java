package br.com.mercadinhoprovidence.dto.funcionario;

import br.com.mercadinhoprovidence.model.Employee;
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

    public FuncionarioResponseDto(Employee funcionario) {
        this.nome = funcionario.getNome();
        this.codigoVerificador = funcionario.getCodigoVerificador();
    }

}
