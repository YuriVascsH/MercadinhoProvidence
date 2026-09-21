package br.com.mercadinhoprovidence.view;

import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.mercadinhoprovidence.config.ScreenNavigator;
import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dto.login.LoginRequestDto;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;
import br.com.mercadinhoprovidence.view.component.global.SubmitButton;
import net.miginfocom.swing.MigLayout;

public class ScreenLogin extends JPanel {

    private JTextField userField;
    private JPasswordField passwordField;
    private JButton btnAcessar;

    private final LoginController loginController;
    private final ScreenNavigator navigator;

    public ScreenLogin(ScreenNavigator navigator, LoginController loginController) {
        this.navigator = navigator;
        this.loginController = loginController;
        setupUI();
    }

    private void setupUI() {
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        
        JPanel cardPanel = createCardPanel();
        
        cardPanel.add(createHeader(), "wrap");
        createFormFields(cardPanel);
        
        btnAcessar = new SubmitButton("Acessar");
        cardPanel.add(btnAcessar, "gapy 20, h 42!");

        ActionListener loginAction = e -> handleLoginAttempt();
        btnAcessar.addActionListener(loginAction);
        userField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);

        add(cardPanel);
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel(new MigLayout("wrap, fillx, insets 35 45 40 45", "fill, 280:320"));
        card.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,2%);" +
                "[dark]background:lighten(@background,3%)");
        return card;
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new MigLayout("wrap, fillx, insets 0", "fill"));
        headerPanel.setOpaque(false);

        JLabel lbTitle = new JLabel("Mercadinho Providence");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

        JLabel lbDescription = new JLabel("Por favor, faça login para acessar o caixa");
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        headerPanel.add(lbTitle);
        headerPanel.add(lbDescription, "gapbottom 15");
        return headerPanel;
    }

    private void createFormFields(JPanel container) {
        userField = new JTextField();
        InputUtils.limitDigitsNumber(userField, 20);
        userField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: 1023");

        passwordField = new JPasswordField();
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Digite sua senha");
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        container.add(new JLabel("ID do Funcionário"), "gapy 6");
        container.add(userField, "h 38!");

        container.add(new JLabel("Senha"), "gapy 6");
        container.add(passwordField, "h 38!");
    }

    /**
     * Lógica da aplicação
     */
    private void handleLoginAttempt() {
        String idString = userField.getText().trim();
        char[] passwordChars = passwordField.getPassword();

        if (idString.isEmpty() || passwordChars.length == 0) {
            AlertUtils.showWarning("Campos faltando", "Por favor, preencha todos os campos.");
            if (idString.isEmpty()) {
                userField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
            return;
        }

        try {
            int idInt = Integer.parseInt(idString);

            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setId(idInt);
            loginRequestDto.setSenha(new String(passwordChars));

            this.loginController.firstStage(loginRequestDto);

            clearFields();

            this.navigator.codeVerify();

        } catch (NumberFormatException ex) {
            AlertUtils.showError("Erro no ID", "O ID do funcionário deve conter apenas números válidos.");
            cleanAndFocus();
        } catch (IllegalArgumentException ex) {
            AlertUtils.showError("Erro de Login", ex.getMessage());
            cleanAndFocus();
        } catch (IllegalStateException ex) {
            AlertUtils.showError("Erro de Estado", "Ocorreu um erro na ordem de login. Detalhes: " + ex.getMessage());
            cleanAndFocus();
        } catch (Exception ex) {
            AlertUtils.showError("Erro Inesperado", "Ocorreu um problema ao tentar fazer login.",
                    "Detalhes: " + ex.getMessage() + "\nPor favor, contate o suporte.");
            ex.printStackTrace();
            cleanAndFocus();
        } finally {
            Arrays.fill(passwordChars, '0');
        }
    }

    /**
     * Método auxiliar para limpar os campos.
     */
    private void clearFields() {
        userField.setText("");
        passwordField.setText("");
    }

    /**
     * Método auxiliar para limpar os campos e focar.
     */
    private void cleanAndFocus() {
        userField.setText("");
        passwordField.setText("");
        userField.requestFocus();
    }
}