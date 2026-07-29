package br.com.mercadinhoprovidence;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;

import br.com.mercadinhoprovidence.config.AppContainer;
import br.com.mercadinhoprovidence.view.TelaLogin;

public class MainApplication {

    private JFrame janelaPrincipal; 
    
    private CardLayout cardLayout;   
    
    private JPanel containerDasTelas;
    
    private AppContainer container;

    public void iniciarSistema() {
        FlatLightLaf.setup();

        this.container = new AppContainer();

        janelaPrincipal = new JFrame("Mercadinho Providence");
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        containerDasTelas = new JPanel(cardLayout);
        janelaPrincipal.add(containerDasTelas);

        TelaLogin telaLogin = new TelaLogin(this, container.getLoginController());
        
        containerDasTelas.add(telaLogin, "TELA_LOGIN");
        
        mostrarTelaLogin();
    }

    public void mostrarTelaLogin() {
        janelaPrincipal.setResizable(false); // Bloqueia o tamanho para o login não esticar
        cardLayout.show(containerDasTelas, "TELA_LOGIN"); // Exibe a carta do login
        
        janelaPrincipal.pack(); // Faz a janela encolher até o tamanho exato do card de login
        janelaPrincipal.setLocationRelativeTo(null); // Centraliza no meio do monitor
        janelaPrincipal.setVisible(true);
    }

    public void mostrarTelaPrincipalPDV() {
        janelaPrincipal.setResizable(true); // Permite redimensionar o PDV
        cardLayout.show(containerDasTelas, "TELA_PDV"); // Exibe a carta do PDV
        
        janelaPrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza em tela cheia
    }

    public void mostrarTelaCodigoVerificador(Object loginController) {

    }

    public static void main(String[] args) {
        // O ponto de entrada do Java que inicia tudo
        SwingUtilities.invokeLater(() -> {
            MainApplication app = new MainApplication();
            app.iniciarSistema();
        });
    }
}