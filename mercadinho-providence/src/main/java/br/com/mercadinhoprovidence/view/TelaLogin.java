package br.com.mercadinhoprovidence.view;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dto.login.LoginRequestDto;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;
import net.miginfocom.swing.MigLayout;

public class TelaLogin extends JPanel {

        private JTextField userField;
        private JPasswordField passwordField;
        private final LoginController loginController;
        private JButton button;
        private MainApplication mainApplication;

        public TelaLogin(MainApplication mainApplication, LoginController loginController) {
                this.mainApplication = mainApplication;
                this.loginController = loginController;
                setupUI();
        }

        // Não mexer aqui
        /**
         * Este método é responsável por montar a interface gráfica
         * usando MigLayout e FlatLaf.
         */
        private void setupUI() {
                // Centraliza o card perfeitamente na tela
                setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

                // Inicializa os componentes de entrada
                userField = new JTextField();
                InputUtils.limitDigitsNumber(userField, 30);
                passwordField = new JPasswordField();
                button = new JButton("Acessar");

                // Aplica os placeholders modernos e o botão de "olhinho" na senha
                userField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: 1023");
                passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Digite sua senha");
                passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

                // Cria o "Card" centralizado com bordas arredondadas e leve fundo customizado
                JPanel cardPanel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 40 45", "fill, 280:320"));
                cardPanel.putClientProperty(FlatClientProperties.STYLE, "" +
                                "arc:20;" +
                                "[light]background:darken(@background,2%);" +
                                "[dark]background:lighten(@background,3%)");

                // Títulos do Card
                JLabel lbTitle = new JLabel("Mercadinho Providence");
                lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

                JLabel lbDescription = new JLabel("Por favor, faça login para acessar o caixa");
                lbDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                                "[light]foreground:lighten(@foreground,30%);" +
                                "[dark]foreground:darken(@foreground,30%)");

                // Estilização do Botão de Ação com Verde Moderno e Cantos Arredondados
                button.putClientProperty(FlatClientProperties.STYLE, "" +
                                "background:#2ecc71;" +             // Cor de fundo verde
                                "foreground:#ffffff;" +             // Texto branco
                                "hoverBackground:darken(#2ecc71,10%);" + // Escurece 10% no hover
                                "pressedBackground:darken(#2ecc71,20%);" + // Escurece 20% ao clicar
                                "arc:10;" +                         // Arredondamento suave dos cantos
                                "borderWidth:0;" +
                                "focusWidth:0;" +
                                "innerFocusWidth:0;" +
                                "font:bold +1");                    // Texto em negrito e ligeiramente maior

                // Eventos de clique e atalho de ENTER
                ActionListener loginAction = e -> handleLoginAttempt();
                button.addActionListener(loginAction);

                // No Swing, dar um addActionListener no JTextField faz ele disparar a ação ao apertar ENTER automaticamente!
                userField.addActionListener(loginAction);
                passwordField.addActionListener(loginAction);

                // Montagem do Layout dentro do Card
                cardPanel.add(lbTitle);
                cardPanel.add(lbDescription, "gapbottom 15");

                cardPanel.add(new JLabel("ID do Funcionário"), "gapy 6");
                cardPanel.add(userField, "h 38!"); // Um pouco mais alto para melhor ergonomia de toque/clique

                cardPanel.add(new JLabel("Senha"), "gapy 6");
                cardPanel.add(passwordField, "h 38!");

                cardPanel.add(button, "gapy 25, h 42!"); // Destaca o botão com espaçamento superior maior

                // Adiciona o card ao container principal
                add(cardPanel);
        }

        /**
         * Lógica
         * 
         */
        public void handleLoginAttempt() {
                String idString = userField.getText().trim();
                String passwordString = new String(passwordField.getPassword()).trim();
              
                if (idString.isEmpty() || passwordString.isEmpty()) {
                        AlertUtils.showWarning("Campos faltando", "Por favor, preencha todos os campos.");
                        if (idString.isEmpty()) {
                                userField.requestFocus();
                        } else {
                                passwordField.requestFocus();
                        }
                        return;
                }

                int idInt;
                try {
                        idInt = Integer.parseInt(idString);
                } catch (NumberFormatException ex) {
                        AlertUtils.showError("Erro no ID", "O ID do funcionário deve conter apenas números válidos.");
                        limparCamposeFocar();
                        return;
                }

                try {
                        // Passo 1 - Criação do Objeto e colocando suas informações. Ok!
                        LoginRequestDto loginRequestDto = new LoginRequestDto();
                        loginRequestDto.setId(idInt);
                        loginRequestDto.setSenha(passwordString);

                        // Passo 2 - Passando o meu objeto do controller para a primeira etapa
                        // Login requestDto está correto, 
                        this.loginController.primeiraEtapa(loginRequestDto);

                        if (mainApplication != null) {
                                System.out.println("Credenciais válidas. Redirecionando para a 2ª tela de autenticação...");
                                mainApplication.mostrarTelaCodigoVerificador(this.loginController);
                        }

                } catch (IllegalArgumentException ex) {
                        AlertUtils.showError("Erro de Login", ex.getMessage());
                        limparCamposeFocar();
                } catch (IllegalStateException ex) {
                        AlertUtils.showError("Erro de Estado", "Ocorreu um erro na ordem de login. Detalhes: " + ex.getMessage());
                        limparCamposeFocar();
                } catch (Exception ex) {
                        AlertUtils.showError("Erro Inesperado", "Ocorreu um problema ao tentar fazer login.",
                                        "Detalhes: " + ex.getMessage() + "\nPor favor, contate o suporte.");
                        ex.printStackTrace();
                        limparCamposeFocar();
                }
        }

        private void limparCamposeFocar() {
                userField.setText("");
                passwordField.setText("");
                userField.requestFocus();
        }
}