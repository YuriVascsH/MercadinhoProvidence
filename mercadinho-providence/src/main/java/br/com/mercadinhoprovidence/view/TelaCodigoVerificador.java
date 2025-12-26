package br.com.mercadinhoprovidence.view;


import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.dto.login.LoginVerificationRequestDto;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;
import br.com.mercadinhoprovidence.view.component.TitleComponents;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.Objects;

public class TelaCodigoVerificador {

    private TextField codeField;
    private final MainApplication mainApplication;
    private final LoginController loginController;
    @Getter
    private Scene scene;
    private Button buttonVerify;

    /**
     * Construtor para TelaCodigoVerificador
     *
     * @param mainApplication A instância da aplicação princiapal para navegação.
     * @param loginController   O controlador responsável pela lógica de login e autenticação
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
     * Este método é responsável por montar a interface gráfica
     * e criar a Scene para esta view.
     */
    private void setupUI() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Adição do conteúdo
        root.setTop(TitleComponents.createHeaderBox());
        root.setCenter(createCodeContent());
        root.setBottom(createButtonBox());

        // Definindo o tamnho da tela e a Scene
        this.scene = new Scene(root, 500, 300);

        this.scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/LoginStyles.css")).toExternalForm());
    }

    /**
     * Retorna um VBox (Contendo o campo verificador).
     * @return VBox contendo o campo de código.
     */
    private VBox createCodeContent() {
        VBox contentBox = new VBox(30);
        contentBox.setId("code-content-box");
        contentBox.setAlignment(Pos.CENTER);

        this.codeField = new TextField();
        this.codeField.setPromptText("Código verificador");
        InputUtils.limitDigits(this.codeField, 6);
        this.codeField.getStyleClass().add("text-field");

        this.codeField.setOnKeyPressed(this::handleEnterKey);
        contentBox.getChildren().addAll(this.codeField);
        return contentBox;
    }

    /**
     * Cria e retorna um VBox que contém o botão para submeter o código verificador.
     * @return VBox contendo o botão de verificação.
     */
    private VBox createButtonBox() {
        VBox buttonBox = new VBox();
        buttonBox.setId("button-box");
        buttonVerify = new Button("Verificar Código");
        buttonVerify.getStyleClass().add("login-button");

        // Define a ação do botão para chamar o metodo de tentativa de código
        buttonVerify.setOnAction(e -> handleCodeAttempt());

        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(buttonVerify);
        return buttonBox;
    }

    /**
     * Lida com a tentativa de autenticação da segunda etapa (código verificador).
     * Em caso de sucesso, chama a MainApplication para exibir a TelaInicialView.
     */
    public void handleCodeAttempt() {
        String codeString = codeField.getText().trim();

        if (codeString.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Por favor, insira o código verificador.");
            codeField.requestFocus();
            return;
        }

        int codeInt;
        try {
            codeInt = Integer.parseInt(codeString);
        } catch (NumberFormatException ex) {
            AlertUtils.showError("Erro de Entrada", "O código verificador deve ser um número válido.");
            codeField.clear();
            codeField.requestFocus();
            return;
        }

        try {
            LoginVerificationRequestDto loginVerificationRequestDto = new LoginVerificationRequestDto();
            loginVerificationRequestDto.setCodigoVerificador(codeInt);
            LoginResponseDto funcionarioLogado = this.loginController.segundaEtapa(loginVerificationRequestDto);
            AlertUtils.showSuccess("Login Bem-sucedido!", "Bem vindo, " + funcionarioLogado.getName() + "!");
            mainApplication.mostrarTelaInicial(funcionarioLogado);

        } catch (IllegalArgumentException ex) {
            // Código inválido, mas mantém usuário da primeira etapa
            AlertUtils.showError("Código incorreto", "O código verificador está errado. Tente novamente.");
            codeField.clear();
            codeField.requestFocus();
        } catch (IllegalStateException ex) {
            // Sessão realmente expirada, volta para login
            AlertUtils.showError("Sessão Expirada", ex.getMessage() + "\nPor favor, faça login novamente.");
            mainApplication.mostrarTelaLogin();
            codeField.clear();
        } catch (Exception ex) {
            AlertUtils.showError("Erro Inesperado", "Ocorreu um problema ao tentar verificar o código.",
                    "Detalhes: " + ex.getMessage() + "\nPor favor, tente novamente mais tarde ou contate o suporte.");
            codeField.clear();
            codeField.requestFocus();
        }
    }

    /**
     * Lida com o evento de pressionar a tecla ENTER, aciona o botão de verificação.
     * @param event O evento do teclado
     */
    public void handleEnterKey(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            buttonVerify.fire();
            event.consume();
        }
    }
}