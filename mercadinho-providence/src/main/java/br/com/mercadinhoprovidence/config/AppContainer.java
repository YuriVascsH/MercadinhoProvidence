package br.com.mercadinhoprovidence.config;

import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dao.FuncionarioDao;
import br.com.mercadinhoprovidence.service.LoginService;

public class AppContainer {

    private FuncionarioDao funcionarioDao;
    private LoginService loginService;
    private LoginController loginController;

    public FuncionarioDao getFuncionarioDao() {
        if (funcionarioDao == null) {
            this.funcionarioDao = new FuncionarioDao();
        }

        return funcionarioDao;
    }

    public LoginService getLoginService() {
        if (loginService == null) {
            loginService = new LoginService(getFuncionarioDao());
        }
        return loginService;
    }

    public LoginController getLoginController() {
        if (loginController == null) {
            loginController = new LoginController(getLoginService());
        }
        return loginController;
    }

}
