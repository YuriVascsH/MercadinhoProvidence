package br.com.mercadinhoprovidence.controller;

import br.com.mercadinhoprovidence.dto.login.LoginRequestDto;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.dto.login.LoginVerificationRequestDto;
import br.com.mercadinhoprovidence.exceptions.LoginFluxoIncorretoException;
import br.com.mercadinhoprovidence.mapper.LoginMapper;
import br.com.mercadinhoprovidence.model.Employee;
import br.com.mercadinhoprovidence.service.LoginService;

public class LoginController {

    private final LoginService loginService;
    private Employee partialEmployeeLogin;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
        this.partialEmployeeLogin = null;
    }

    /**
     * Realiza a validação da primeira etapa de verificação.
     *
     * @param loginRequestDto fornecido pelo usuário contendo(Id e senha)
     */
    public void firstStage(LoginRequestDto loginRequestDto) throws LoginFluxoIncorretoException {
        this.partialEmployeeLogin = loginService.validarCredenciais(loginRequestDto.getId(), loginRequestDto.getSenha());
    }

    /**
     * Realiza a validação da segunda etapa do processo de login
     *
     * @param loginVerificationRequestDto fornecido pelo usuário(Código verificador)
     * @return Retorna o loginResponseDto(nome, cargo e codigoVerificador)
     */
    public LoginResponseDto secondStage(LoginVerificationRequestDto loginVerificationRequestDto) {
        if(this.partialEmployeeLogin == null) {
            throw new LoginFluxoIncorretoException("A primeira etapa não foi realizada.");
        }

        if (loginService.validarCodigoVerificador(this.partialEmployeeLogin, loginVerificationRequestDto.getCodigoVerificador())) {
            LoginResponseDto response = LoginMapper.toLoginResponseDto(partialEmployeeLogin);
            partialEmployeeLogin = null;
            return response;
        } else {
            throw new LoginFluxoIncorretoException("O código verificador está incorreto");
        }

    }

    /**
     * Cancela o fluxo atual e limpa a sessão parcial (ex: ao clicar em voltar/cancelar).
     */
    public void resetState() {
        this.partialEmployeeLogin = null;
    }

}
