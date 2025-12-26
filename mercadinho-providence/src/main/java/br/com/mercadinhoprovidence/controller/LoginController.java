package br.com.mercadinhoprovidence.controller;

import br.com.mercadinhoprovidence.Service.LoginService;
import br.com.mercadinhoprovidence.dto.login.LoginRequestDto;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.dto.login.LoginVerificationRequestDto;
import br.com.mercadinhoprovidence.exceptions.LoginFluxoIncorretoException;
import br.com.mercadinhoprovidence.model.Funcionario;

public class LoginController {

    private final LoginService loginService;
    private Funcionario funcionarioLoginParcial;

    public LoginController() {
        this.loginService = new LoginService();
        this.funcionarioLoginParcial = null;
    }

    /**
     * Realiza a validação da primeira etapa de verificação.
     *
     * @param loginRequestDto fornecido pelo usuário contendo(Id e senha)
     */
    public void primeiraEtapa(LoginRequestDto loginRequestDto) throws LoginFluxoIncorretoException {
        this.funcionarioLoginParcial = loginService.validarCredenciais(loginRequestDto.getId(), loginRequestDto.getSenha());
    }

    /**
     * Realiza a validação da segunda etapa do processo de login
     *
     * @param loginVerificationRequestDto fornecido pelo usuário(Código verificador)
     * @return Retorna o loginResponseDto(nome, cargo e codigoVerificador)
     */
    public LoginResponseDto segundaEtapa(LoginVerificationRequestDto loginVerificationRequestDto) {
        if(this.funcionarioLoginParcial == null) {
            throw new LoginFluxoIncorretoException("A primeira etapa não foi realizada.");
        }

        if (loginService.validarCodigoVerificador(this.funcionarioLoginParcial, loginVerificationRequestDto.getCodigoVerificador())) {
            return loginService.converterParaLoginResponseDto(funcionarioLoginParcial);
        } else {
            throw new LoginFluxoIncorretoException("O código verificador está incorreto");
        }

    }

}
