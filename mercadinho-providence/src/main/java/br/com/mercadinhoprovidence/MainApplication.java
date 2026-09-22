package br.com.mercadinhoprovidence;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;

import br.com.mercadinhoprovidence.config.AppContainer;
import br.com.mercadinhoprovidence.config.ScreenNavigator;

public class MainApplication {

	/**
	 * Método principal da aplicação. 
	 */
	public static void main(String[] args) {
		// O ponto de entrada do Java que inicia tudo
		SwingUtilities.invokeLater(() -> {
			FlatLightLaf.setup();
			AppContainer container = new AppContainer();
			JFrame screenPrimary = new JFrame("Mercadinho Providence");
			screenPrimary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			CardLayout cardLayout = new CardLayout();
			JPanel containerScreens = new JPanel(cardLayout);
			screenPrimary.add(containerScreens);

			ScreenNavigator navigator = new ScreenNavigator(screenPrimary, cardLayout, containerScreens);

			navigator.registerScreen(container.createScreenLogin(navigator), ScreenNavigator.SCREEN_LOGIN);
			navigator.registerScreen(container.createScreenCodeVerify(navigator), ScreenNavigator.SCREEN_CODE_VERIFY);
			navigator.registerScreen(container.createScreenHome(navigator), ScreenNavigator.SCREEN_HOME);
			//navigator.registerScreen(container.createScreenPdv(navigator), ScreenNavigator.SCREEN_PDV);

			navigator.login();
		});
	}
}