package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.components.TitleHeaderPane;
import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dto.LoginRequestDto;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TelaLogin {

    private TextField userField;
    private PasswordField passwordField;
    private LoginController loginController;
    private Button button;

    private Scene scene;
    private MainApplication mainApplication;

    public TelaLogin(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
        this.loginController = new LoginController();
        setupUI();
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Este método é responsável por montar a interface gráfica
     * e criar a Scene para esta view.
     * ** Conteúdo do antigo start() deve vir para cá, exceto a manipulação do Stage **
     */
    private void setupUI() {
        // Layout da tela
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Adição do conteúdo
        root.setTop(TitleHeaderPane.createHeaderBox());
        root.setCenter(createLoginContent());
        root.setBottom(createButtonBox());

        this.scene = new Scene(root, 500, 480);
        this.scene.getStylesheets().add(getClass().getResource("/styles/LoginStyles.css").toExternalForm());

    }

    /**
     * Cria e retorna um VBox (contendo o formulário da aplicação) que servirá como o contêiner para os elementos
     * @return VBox contendo o placeholder para o conteúdo de login.
     */
    private VBox createLoginContent() {
        VBox contentBox = new VBox(30);
        contentBox.setId("login-content-box");
        contentBox.setAlignment(Pos.CENTER);

        // Campo id do funcionário
        this.userField = new TextField();
        this.userField.setPromptText("ID do funcionário");
        InputUtils.limitDigits(this.userField, 30);
        this.userField.getStyleClass().add("text-field");

        // Campo Senha do funcionário
        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Senha do funcionário");
        this.passwordField.getStyleClass().add("password-field");

        userField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);
        contentBox.getChildren().addAll(this.userField, this.passwordField);
        return contentBox;
    }

    /**
     * Cria e retorna um VBox (contendo o botão de logar) que serve para verificar os dados inseridos e chamar a TelaCodigoVerificador
     * @return VBox contendo o botão de login
     */
    private VBox createButtonBox() {
        VBox buttonBox = new VBox();
        buttonBox.setId("button-box");
        this.button = new Button("Acessar");
        button.getStyleClass().add("login-button");

        // Chamada da função de tentativa de login
        button.setOnAction(e -> handleLoginAttempt());

        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(button);
        return buttonBox;
    }


    public void handleLoginAttempt() {
        String idString = userField.getText().trim();
        String passwordString = passwordField.getText().trim();
        System.out.println("DEBUG UI: Senha digitada (trim): '" + passwordString + "'");

        // Verifica se os campos de id e senha estão vazios
        if (idString.isEmpty() || passwordString.isEmpty()){
            AlertUtils.showWarning("Campos faltando","Por favor, preencha todos os campos.");
            userField.requestFocus();
            return;
        }

        int idInt;
        // Conversão e validação do ID numérico
        try {
            idInt = Integer.parseInt(idString);
        } catch (NumberFormatException ex) {
            AlertUtils.showError("Erro no ID.", "O ID do funcionário deve ser um número válido.");
            userField.clear();
            passwordField.clear();
            userField.requestFocus();
            return;
        }

        try {
            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setId(idInt);
            loginRequestDto.setSenha(passwordString);
            // Chamada do primeira etapa de logar usando o loginController
            this.loginController.primeiraEtapa(loginRequestDto);

            // Se autenticarPrimeiraEtapa for bem-sucedido:
            mainApplication.mostrarTelaCodigoVerificador(this.loginController);

        } catch (IllegalArgumentException ex) {
            // Captura exceções específicas de Login/Regra de Negócio
            AlertUtils.showError("Erro de Login", ex.getMessage());
            userField.clear();
            passwordField.clear();
            userField.requestFocus();
        } catch (IllegalStateException ex) {
            AlertUtils.showError("Erro de Estado", "Ocorreu um erro na ordem de login. Detalhes: " + ex.getMessage());
            userField.clear();
            passwordField.clear();
            userField.requestFocus();
        } catch (Exception ex) {
            AlertUtils.showError("Erro Inesperado", "Ocorreu um problema ao tentar fazer login.","Detalhes: " + ex.getMessage() + "\nPor favor, tente novamente mais tarde ou contate o suporte.");
            ex.printStackTrace();
            userField.clear();
            passwordField.clear();
            userField.requestFocus();
        }
    }

    /**
     * Lida com o evento de pressionar a tecla ENTER, aciona o botão de login
     *
     * @param event O evento do teclado
     */
    public void handleEnterKey(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            button.fire();
            event.consume();
        }
    }
}
