package br.com.mercadinhoprovidence.view;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.mercadinhoprovidence.config.ScreenNavigator;
import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.dto.login.LoginVerificationRequestDto;
import br.com.mercadinhoprovidence.exceptions.LoginFluxoIncorretoException;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;
import br.com.mercadinhoprovidence.view.component.global.SubmitButton;
import net.miginfocom.swing.MigLayout;

public class ScreenCodeVerify extends JPanel {

    private JTextField codeField;
    private JButton btnVerify;

    private final ScreenNavigator navigator;
    private final LoginController loginController;

    public ScreenCodeVerify(ScreenNavigator navigator, LoginController loginController) {
        if (loginController == null) {
            throw new IllegalArgumentException("LoginController não pode ser nulo para ScreenCodeVerify.");
        }
        this.navigator = navigator;
        this.loginController = loginController;
        setupUI();
    }

    private void setupUI() {
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        JPanel cardPanel = createCardPanel();

        cardPanel.add(createHeader(), "wrap");
        createFormFields(cardPanel);

        btnVerify = new SubmitButton("Verificar Código");
        cardPanel.add(btnVerify, "gapy 20, h 42!");

        // Ação do botão de verificação e tecla ENTER
        ActionListener verifyAction = e -> handleCodeAttempt();
        btnVerify.addActionListener(verifyAction);
        codeField.addActionListener(verifyAction);

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

        JLabel lbTitle = new JLabel("Verificação de Segurança");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

        JLabel lbDescription = new JLabel("Informe o código verificador enviado/gerado");
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        headerPanel.add(lbTitle);
        headerPanel.add(lbDescription, "gapbottom 15");
        return headerPanel;
    }

    private void createFormFields(JPanel container) {
        codeField = new JTextField();
        InputUtils.limitDigitsNumber(codeField, 6);
        codeField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: 123456");

        container.add(new JLabel("Código Verificador"), "gapy 6");
        container.add(codeField, "h 38!");
    }

    private void handleCodeAttempt() {
        String codeString = codeField.getText().trim();

        if (codeString.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Por favor, insira o código verificador.");
            codeField.requestFocus();
            return;
        }

        try {
            int codeInt = Integer.parseInt(codeString);

            LoginVerificationRequestDto dto = new LoginVerificationRequestDto();
            dto.setCodigoVerificador(codeInt);

            // Chama a segunda etapa no controller
            LoginResponseDto funcionarioLogado = this.loginController.secondStage(dto);

            AlertUtils.showSuccess("Login Bem-sucedido!", "Bem-vindo(a), " + funcionarioLogado.getName() + "!");

            clearField();
            // Redireciona para o PDV via Navigator
            this.navigator.pdv();

        } catch (NumberFormatException ex) {
            AlertUtils.showError("Erro de Entrada", "O código verificador deve conter apenas números válidos.");
            clearAndFocus();
        } catch (LoginFluxoIncorretoException ex) {
            // Se o fluxo foi quebrado (ex: código incorreto ou 1ª etapa zerada)
            AlertUtils.showError("Código Incorreto", ex.getMessage());
            clearAndFocus();
        } catch (IllegalArgumentException ex) {
            AlertUtils.showError("Erro de Validação", ex.getMessage());
            clearAndFocus();
        } catch (IllegalStateException ex) {
            AlertUtils.showError("Sessão Expirada", ex.getMessage() + "\nPor favor, faça login novamente.");
            clearField();
            this.navigator.login(); 
        } catch (Exception ex) {
            AlertUtils.showError("Erro Inesperado", "Ocorreu um problema ao tentar verificar o código.",
                    "Detalhes: " + ex.getMessage() + "\nPor favor, contate o suporte.");
            ex.printStackTrace();
            clearAndFocus();
        }
    }

    private void clearField() {
        codeField.setText("");
    }

    private void clearAndFocus() {
        clearField();
        codeField.requestFocus();
    }
}