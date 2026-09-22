package br.com.mercadinhoprovidence.service;

import br.com.mercadinhoprovidence.dao.FuncionarioDao;
import br.com.mercadinhoprovidence.model.Employee;

public class LoginService {

    private final FuncionarioDao funcionarioDao;

    public LoginService(FuncionarioDao funcionarioDao) {
        this.funcionarioDao = funcionarioDao;
    }

    /**
     * Valida o funcionario por meio de seu Id e senha
     *
     * @param id  fornecido pelo usuário
     * @param senha fornecido pelo usuário
     * @return uma exceção ou o funcionario
     */
    public Employee validarCredenciais(int id, String senha) {
        Employee funcionario = funcionarioDao.buscarPorIdSenha(id, senha.trim());
        if (funcionario == null) {
            throw new IllegalArgumentException("ID e/ou senha inválidos.");
        }

        if (!funcionario.getAtivo()) {
            throw new IllegalArgumentException("Funcionário inativo.");
        }
        funcionario.setSenha(null);
        return funcionario;
    }

    /**
     * Valida as credencias de código verificador do funcionário.
     *
     * @return retorna um valor boolean
     */
    public Boolean validarCodigoVerificador(Employee loginResponseDto, int codigoVerficador) {
       return loginResponseDto.getCodigoVerificador() == codigoVerficador;
   }

}
