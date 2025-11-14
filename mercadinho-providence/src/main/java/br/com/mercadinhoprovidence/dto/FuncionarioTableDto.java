package br.com.mercadinhoprovidence.dto;

import br.com.mercadinhoprovidence.model.Funcionario;
import br.com.mercadinhoprovidence.model.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioTableDto {

    private Integer idFuncionario;
    private Integer codigoFuncionario;
    private String nome;
    private String cpf;
    private Cargo cargo;
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private Boolean ativo;

    public FuncionarioTableDto(Funcionario funcionario) {
        this.idFuncionario = funcionario.getIdFuncionario();
        this.codigoFuncionario = funcionario.getCodigoVerificador();
        this.nome = funcionario.getNome();
        this.cpf = funcionario.getCpf();
        this.cargo = funcionario.getCargo();
        this.salario = funcionario.getSalario();
        this.dataAdmissao = funcionario.getDataAdmissao();
        this.ativo = funcionario.getAtivo();
    }
}
