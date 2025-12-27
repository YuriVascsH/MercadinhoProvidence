package br.com.mercadinhoprovidence.Service;

import br.com.mercadinhoprovidence.dao.FuncionarioDao;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.model.Funcionario;

public class LoginService {

    private final FuncionarioDao funcionarioDao = new FuncionarioDao();

    /**
     * Valida o funcionario por meio de seu Id e senha
     *
     * @param id  fornecido pelo usuário
     * @param senha fornecido pelo usuário
     * @return uma exceção ou o funcionario
     */
    public Funcionario validarCredenciais(int id, String senha) {
        Funcionario funcionario = funcionarioDao.buscarPorIdSenha(id, senha.trim());
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
    public Boolean validarCodigoVerificador(Funcionario loginResponseDto, int codigoVerficador) {
       return loginResponseDto.getCodigoVerificador() == codigoVerficador;
   }

   /**
    * Converte a classe funcionario para LoginResponseDto
    *
    * @param funcionario que vem do metodo validarCredenciais
    *
    * */
    public LoginResponseDto converterParaLoginResponseDto(Funcionario funcionario) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setName(funcionario.getNome());
        loginResponseDto.setCodigoVerificador(funcionario.getCodigoVerificador());
        loginResponseDto.setCargo(funcionario.getCargo());
        return loginResponseDto;
   }
}
