package br.com.mercadinhoprovidence.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.mercadinhoprovidence.model.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder 
@NoArgsConstructor 
@AllArgsConstructor
public class Funcionario {

    private Integer idFuncionario;
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

}
