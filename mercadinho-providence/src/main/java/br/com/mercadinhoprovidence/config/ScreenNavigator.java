package br.com.mercadinhoprovidence.config;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;

public class ScreenNavigator {

    public static final String SCREEN_LOGIN = "SCREEN_LOGIN";
    public static final String SCREEN_PDV = "SCREEN_PDV";
    public static final String SCREEN_CODE_VERIFY = "SCREEN_CODE_VERIFY";

    private final JFrame screenPrimary;
    private final CardLayout cardLayout;
    private final JPanel containerScreens;

    public ScreenNavigator(JFrame screenPrimary, CardLayout cardLayout, JPanel containerScreens) {
        this.screenPrimary = screenPrimary;
        this.cardLayout = cardLayout;
        this.containerScreens = containerScreens;
    }

    public void registerScreen(JPanel screen, String nameKey) {
        this.containerScreens.add(screen, nameKey);

    }

    public void login() {
        screenPrimary.setResizable(false);
        cardLayout.show(containerScreens, SCREEN_LOGIN);
        screenPrimary.pack();
        screenPrimary.setLocationRelativeTo(null);
        screenPrimary.setVisible(true);
    }

    public void codeVerify() {
        screenPrimary.setResizable(false);
        cardLayout.show(containerScreens, SCREEN_CODE_VERIFY);
        screenPrimary.pack();
        screenPrimary.setLocationRelativeTo(null);
    }

    public void pdv(LoginResponseDto loginResponseDto) {
        screenPrimary.setResizable(false);
        cardLayout.show(containerScreens, SCREEN_PDV);
        screenPrimary.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

}
