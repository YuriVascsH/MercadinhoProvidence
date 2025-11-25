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
	public Integer getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Integer getCodigoVerificador() {
		return codigoVerificador;
	}
	public void setCodigoVerificador(Integer codigoVerificador) {
		this.codigoVerificador = codigoVerificador;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}
	public void setDataAdmissao(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	public Cargo getCargo() {
		return cargo;
	}
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	public BigDecimal getSalario() {
		return salario;
	}
	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public LocalDateTime getUltimaVenda() {
		return ultimaVenda;
	}
	public void setUltimaVenda(LocalDateTime ultimaVenda) {
		this.ultimaVenda = ultimaVenda;
	}
    
    
}
