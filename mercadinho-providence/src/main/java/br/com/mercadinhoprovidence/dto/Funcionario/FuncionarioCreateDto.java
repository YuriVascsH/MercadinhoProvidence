package br.com.mercadinhoprovidence.dto.Funcionario;

import br.com.mercadinhoprovidence.model.enums.Cargo;
import br.com.mercadinhoprovidence.util.CodigoVerificadorUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FuncionarioCreateDto {

    private Integer codigoVerificador;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataAdmissao;
    private Cargo cargo;
    private BigDecimal salario;
    private String senha;
    private Boolean ativo;
    private LocalDateTime ultimaVenda;

    public FuncionarioCreateDto(String cpf, String nome, LocalDate dataNascimento, String telefone, String email, String endereco, LocalDate dataAdmissao, Cargo cargo, BigDecimal salario, String senha, Boolean ativo) {
        this.codigoVerificador = CodigoVerificadorUtil.gerarCodigoVerificador();
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.dataAdmissao = dataAdmissao;
        this.cargo = cargo;
        this.salario = salario;
        this.senha = senha;
        this.ativo = ativo;
        this.ultimaVenda = null;
    }
}
