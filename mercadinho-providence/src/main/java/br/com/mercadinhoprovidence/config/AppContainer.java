package br.com.mercadinhoprovidence.config;

import java.util.Set;

import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dao.FuncionarioDao;
import br.com.mercadinhoprovidence.service.LoginService;
import br.com.mercadinhoprovidence.view.ScreenCodeVerify;
import br.com.mercadinhoprovidence.view.ScreenHome;
import br.com.mercadinhoprovidence.view.ScreenLogin;

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


    public ScreenLogin createScreenLogin(ScreenNavigator navigator) {
        return new ScreenLogin(navigator, getLoginController()); 
    }

    public ScreenCodeVerify createScreenCodeVerify(ScreenNavigator navigator) {
        return new ScreenCodeVerify(navigator, getLoginController());
    }

    public ScreenHome createScreenHome(ScreenNavigator navigator) {
        return new ScreenHome(navigator, Set.of("ajuda"));
    }

    // public ScreenPdv createScreenPdv(ScreenNavigator navigator) {
    //     return new ScreenPdv(navigator);
    // }

}
