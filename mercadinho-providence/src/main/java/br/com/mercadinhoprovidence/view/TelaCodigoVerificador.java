package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.dto.login.LoginVerificationRequestDto;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelaCodigoVerificador extends JPanel {

    private JTextField codeField;
    private JButton buttonVerify;

    private final MainApplication mainApplication;
    private final LoginController loginController;

    /**
     * Construtor da TelaCodigoVerificador
     */
    public TelaCodigoVerificador(MainApplication mainApplication, LoginController loginController) {
        if (loginController == null) {
            throw new IllegalArgumentException("LoginController não pode ser nulo para TelaCodigoVerificador.");
        }
        this.mainApplication = mainApplication;
        this.loginController = loginController;

        setupUI();
    }

    /**
     * Monta a interface gráfica usando GridBagLayout para centralizar tudo de forma compacta.
     */
    private void setupUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Coloca um componente abaixo do outro automaticamente
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz o componente ocupar a largura disponível
        gbc.weightx = 1.0; // Distribui o espaço horizontal
        gbc.insets = new Insets(0, 0, 10, 0); // Espaçamento inferior padrão entre componentes

        // --- 1. Título ---
        JLabel lblTitulo = new JLabel("Verificação", SwingConstants.CENTER);
        lblTitulo.putClientProperty(FlatClientProperties.STYLE, "font: bold +4");
        gbc.insets = new Insets(0, 0, 20, 0); // Aumenta o espaço abaixo do título
        add(lblTitulo, gbc);

        // --- 2. Campo de Código (Componente centralizado) ---
        codeField = new JTextField(6); // Definimos 6 colunas para alinhar com o placeholder
        codeField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "000000");
        codeField.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; " + // Cantos menos arredondados (mais profissional)
                "font: bold +8; " + // Fonte grande e negrito para o código
                "margin: 8,10,8,10; " + // Espaçamento interno do texto
                "alignment: center; " + // Centraliza o texto digitado
                "showClearButton: true" // Botão para limpar o campo
        );

        // Máscara / Limite de 6 dígitos apenas numéricos
        codeField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null) return;
                // Aceita só números e limita até 6 dígitos
                if (str.matches("\\d+") && (getLength() + str.length()) <= 6) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        // Evento da tecla ENTER para acionar o botão
        codeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodeAttempt();
                }
            }
        });

        gbc.insets = new Insets(0, 30, 20, 30); // Recuo lateral para o campo não ficar colado nas bordas
        add(codeField, gbc);

        // --- 3. Botão de Verificação ---
        buttonVerify = new JButton("Verificar");
        // Estilização FlatLaf do Botão (Compacto e limpo)
        buttonVerify.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; " +
                "background: #007bff; " +
                "focusedBackground: #0056b3; " +
                "foreground: #ffffff; " +
                "font: bold +1; " +
                "margin: 10,0,10,0"); // Aumenta a altura interna do botão

        buttonVerify.addActionListener(e -> handleCodeAttempt());

        gbc.insets = new Insets(0, 30, 0, 30); // Alinha o botão com o campo
        add(buttonVerify, gbc);
    }

    /**
     * Lida com a tentativa de autenticação da segunda etapa (código verificador).
     * (MANTIDO EXATAMENTE COMO SEU CÓDIGO ORIGINAL)
     */
    public void handleCodeAttempt() {
        String codeString = codeField.getText().trim();

        if (codeString.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Por favor, insira o código verificador.");
            InputUtils.limparEFocar(codeField, codeField);
            return;
        }

        int codeInt;
        try {
            codeInt = Integer.parseInt(codeString);
        } catch (NumberFormatException ex) {
            AlertUtils.showError("Erro de Entrada", "O código verificador deve ser um número válido.");
            InputUtils.limparEFocar(codeField, codeField);
            return;
        }

        try {
            LoginVerificationRequestDto requestDto = new LoginVerificationRequestDto();
            requestDto.setCodigoVerificador(codeInt);

            LoginResponseDto funcionarioLogado = this.loginController.segundaEtapa(requestDto);

            AlertUtils.showSuccess("Login Bem-sucedido!", "Bem vindo, " + funcionarioLogado.getName() + "!");

            // Navega para a tela principal/PDV passando o DTO logado
            // mainApplication.mostrarTelaInicial(funcionarioLogado);

        } catch (IllegalArgumentException ex) {
            // Código inválido, mas mantém o usuário logado parcialmente na 1ª etapa
            AlertUtils.showError("Código incorreto", "O código verificador está errado. Tente novamente.");
            InputUtils.limparEFocar(codeField, codeField);
        } catch (IllegalStateException ex) {
            // Sessão/ordem incorreta ou expirada, volta para o login
            AlertUtils.showError("Sessão Expirada", ex.getMessage() + "\nPor favor, faça login novamente.");
            mainApplication.mostrarTelaLogin();
            InputUtils.limparEFocar(codeField, codeField);
        } catch (Exception ex) {
            AlertUtils.showError("Erro Inesperado",
                    "Ocorreu um problema ao tentar verificar o código.\n" +
                            "Detalhes: " + ex.getMessage() + "\nPor favor, contate o suporte.");
            ex.printStackTrace();
            InputUtils.limparEFocar(codeField, codeField);
        }
    }
}