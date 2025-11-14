package br.com.mercadinhoprovidence.dto.Funcionario;

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

}
