package br.com.mercadinhoprovidence.view.dialogs;

import br.com.mercadinhoprovidence.controller.FuncionarioController;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioCreateDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioResponseDto;
import br.com.mercadinhoprovidence.model.enums.Cargo;
//import br.com.mercadinhoprovidence.printer.Impressora;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CadastroFuncionarioDialog {

    private TextField nomeField;
    private TextField cpfField;
    private TextField telefoneField;
    private TextField emailField;
    private TextField enderecoField;
    private ComboBox<String> cargoCombo;
    private TextField salarioField;
    private DatePicker dataNascimentoDatePicker;
    private DatePicker dataAdmissaoPicker;
    private PasswordField senhaField;
    private CheckBox ativoCheck;
    private Stage ownerStage;

    /**
     * Construtor para CadastroFuncionarioDialog.
     * 
     * @param ownerStage O Stage pai desta dialog. Usado para definir a modalidade.
     */
    public CadastroFuncionarioDialog(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    /**
     * Cria e configura a seção de opções (Pesquisar e Cadastrar).
     * <p>
     * Este metodo agora aceita um Runnable que será executado ao cadastrar um
     * funcionário,
     * permitindo a atualização de outras partes da UI, como uma tabela de
     * funcionários.
     * </p>
     *
     * @param onEmployeeRegistered Runnable a ser executado após o cadastro
     *                             bem-sucedido.
     * @return Um BorderPane contendo barra de pesquisa de funcionário e botão de
     *         cadastrar funcionário.
     */
    public static BorderPane createOptionsSection(Runnable onEmployeeRegistered) {

        Label qtdFuncionariosLabel = new Label("Quantidade de Funcionários: ");
        qtdFuncionariosLabel.setStyle("-fx-font-size: 20px;");

        // --- BARRA DE PESQUISA ---
        TextField searchBar = new TextField();
        searchBar.setPromptText("Pesquisar Funcionário pelo CPF");
        searchBar.setPrefWidth(250);

        Button searchButton = new Button("Pesquisar");
        searchButton.setPrefHeight(20);
        searchButton.setOnMouseEntered(e -> searchButton.setCursor(Cursor.HAND));
        searchButton.setOnMouseExited(e -> searchButton.setCursor(Cursor.DEFAULT));

        HBox searchBox = new HBox(10, searchBar, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // --- BOTÃO DE CADASTRAR ---
        Button registerButton = new Button("Cadastrar Funcionário");
        registerButton.setPrefHeight(30);
        registerButton.getStyleClass().add(".button-green");

        registerButton.setOnAction(event -> {
            try {

                System.err.println(
                        "AVISO: createOptionsSection em CadastroFuncionarioDialog não deveria ser estático ou deveria receber o Stage pai.");
                System.err.println("Por favor, mova este método para FuncionariosView e passe o Stage corretamente.");
                Stage tempStage = new Stage();
                CadastroFuncionarioDialog dialog = new CadastroFuncionarioDialog(tempStage);
                dialog.show(onEmployeeRegistered);
            } catch (RuntimeException ex) {
                System.err.println("Erro ao abrir a dialog de cadastro de funcionário: " + ex.getMessage());
            }
        });

        // --- CONTAINER INFERIOR (BorderPane) PARA QTD, PESQUISA E CADASTRAR ---
        BorderPane optionsPane = new BorderPane();
        optionsPane.setCenter(searchBox);
        optionsPane.setRight(registerButton);
        optionsPane.setPadding(new Insets(10, 30, 10, 30));
        return optionsPane;
    }

    /**
     * Cria e retorna o título da dialog "Cadastro de Funcionários".
     *
     * @return BorderPane com o título centralizado.
     */
    public static BorderPane createTitle() {
        Label titleLabel = new Label("Cadastro de Funcionários");
        titleLabel.getStyleClass().add("cadastro-title");
        BorderPane titlePane = new BorderPane(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        titlePane.getStyleClass().add("cadastro-title-pane");
        return titlePane;
    }

    /**
     * Cria e retorna o GridPane contendo o formulário de cadastro de funcionários.
     *
     * @return GridPane com os campos do formulário.
     */
    private GridPane createForm() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30));
        grid.setVgap(20);
        grid.setHgap(50);
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("form-grid");

        // -- Inputs
        nomeField = new TextField();
        InputUtils.limitCharacters(nomeField, 100);
        nomeField.setPromptText("Digite o nome completo");
        addFieldToGrid(grid, "Nome Completo:", nomeField, 0, 0);

        cpfField = new TextField();
        InputUtils.limitDigits(cpfField, 11);
        cpfField.setPromptText("9876543210");
        addFieldToGrid(grid, "CPF:", cpfField, 2, 0);

        telefoneField = new TextField();
        InputUtils.limitDigits(telefoneField, 11);
        telefoneField.setPromptText("Ex: 987654321");
        addFieldToGrid(grid, "Telefone:", telefoneField, 0, 1);

        emailField = new TextField();
        InputUtils.limitCharacters(emailField, 100);
        emailField.setPromptText("exemplo@dominio.com");
        addFieldToGrid(grid, "Email:", emailField, 2, 1);

        enderecoField = new TextField();
        InputUtils.limitCharacters(enderecoField, 200);
        enderecoField.setPromptText("Rua, Número, Bairro, Cidade-UF");
        addFieldToGrid(grid, "Endereço:", enderecoField, 0, 2);

        cargoCombo = new ComboBox<>();
        cargoCombo.getItems().addAll("Operador", "Gerente");
        cargoCombo.setPromptText("Selecione o cargo");
        cargoCombo.setPrefWidth(200);
        addFieldToGrid(grid, "Cargo:", cargoCombo, 2, 2);

        salarioField = new TextField();
        InputUtils.limitCharacters(salarioField, 10);
        salarioField.setPromptText("Ex: 1500.00");
        addFieldToGrid(grid, "Salário:", salarioField, 0, 3);

        dataNascimentoDatePicker = new DatePicker();
        dataNascimentoDatePicker.setPromptText("DD/MM/AAAA");
        dataNascimentoDatePicker.setPrefWidth(200);
        dataNascimentoDatePicker.setConverter(InputUtils.getDateConverter());
        addFieldToGrid(grid, "Nascimento:", dataNascimentoDatePicker, 2, 3);

        dataAdmissaoPicker = new DatePicker();
        dataAdmissaoPicker.setPromptText("DD/MM/AAAA");
        dataAdmissaoPicker.setPrefWidth(200);
        dataAdmissaoPicker.setConverter(InputUtils.getDateConverter());
        addFieldToGrid(grid, "Admissão:", dataAdmissaoPicker, 0, 4);

        senhaField = new PasswordField();
        InputUtils.limitCharacters(senhaField, 20);
        senhaField.setPromptText("Mínimo 6 caracteres");
        addFieldToGrid(grid, "Senha:", senhaField, 2, 4);

        ativoCheck = new CheckBox("Ativo");
        ativoCheck.setSelected(true);
        grid.add(ativoCheck, 1, 5);
        GridPane.setHalignment(ativoCheck, HPos.LEFT);
        GridPane.setMargin(ativoCheck, new Insets(0, 0, 0, 5));

        ColumnConstraints col1Label = new ColumnConstraints();
        col1Label.setHgrow(Priority.NEVER);
        col1Label.setHalignment(HPos.RIGHT);
        col1Label.setMinWidth(100);

        ColumnConstraints col2Field = new ColumnConstraints();
        col2Field.setHgrow(Priority.ALWAYS);
        col2Field.setHalignment(HPos.LEFT);
        col2Field.setMinWidth(220);

        ColumnConstraints col3Label = new ColumnConstraints();
        col3Label.setHgrow(Priority.NEVER);
        col3Label.setHalignment(HPos.RIGHT);
        col3Label.setMinWidth(100);

        ColumnConstraints col4Field = new ColumnConstraints();
        col4Field.setHgrow(Priority.ALWAYS);
        col4Field.setHalignment(HPos.LEFT);
        col4Field.setMinWidth(220);

        grid.getColumnConstraints().addAll(col1Label, col2Field, col3Label, col4Field);

        return grid;
    }

    /**
     * Metodo auxiliar para adicionar um Label e um Control (TextField, ComboBox,
     * etc.) ao GridPane,
     * aplicando os estilos base e listeners de foco.
     *
     * @param grid      O GridPane onde os elementos serão adicionados.
     * @param labelText O texto do Label.
     * @param field     O Control (campo de input) a ser adicionado.
     * @param col       A coluna do GridPane para o Label.
     * @param row       A linha do GridPane para o Label e o Campo.
     */
    private void addFieldToGrid(GridPane grid, String labelText, Control field, int col, int row) {
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");

        grid.add(label, col, row);
        grid.add(field, col + 1, row);

        // Aplica estilo base para todos os tipos de campos relevantes
        if (field instanceof TextField ||
                field instanceof PasswordField ||
                field instanceof ComboBox ||
                field instanceof DatePicker) {

            field.getStyleClass().add("field-base");
        }
    }

    /**
     * Cria e retorna a seção de botões (Cancelar e Cadastrar).
     *
     * @param dialogStage         O Stage da dialog para que os botões possam
     *                            interagir com ele.
     * @param updateTableRunnable Uma Runnable para ser executada após um cadastro
     *                            bem-sucedido (opcional).
     * @return HBox contendo os botões.
     */
    private HBox createButtonsSection(Stage dialogStage, Runnable updateTableRunnable) {
        HBox buttonBox = new HBox(20);
        buttonBox.setPadding(new Insets(20, 0, 10, 0));
        buttonBox.setAlignment(Pos.CENTER);

        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(160);
        btnCancel.setPrefHeight(45);
        btnCancel.getStyleClass().add("button-red");

        btnCancel.setOnAction(e -> dialogStage.close());

        Button btnSave = new Button("Cadastrar");
        btnSave.setPrefWidth(160);
        btnSave.setPrefHeight(45);
        btnSave.getStyleClass().add("button-green");

        btnSave.setOnAction(e -> {
            resetFieldStyles();

            if (validateAllFields()) {
                String cpf = cpfField.getText().trim();
                String nome = nomeField.getText().trim();
                String telefone = telefoneField.getText().trim();
                String email = emailField.getText().trim();
                String endereco = enderecoField.getText().trim();
                String cargoString = cargoCombo.getValue();

                if (email.isEmpty()) {
                    email = null;
                }
                if (endereco.isEmpty()) {
                    endereco = null;
                }

                FuncionarioController funcionarioController = new FuncionarioController();

                if (funcionarioController.verificarCpfExistente(cpf)) {
                    AlertUtils.showError("Erro de Cadastro", "O CPF informado já está cadastrado.");
                    markFieldAsError(cpfField);
                    return;
                }

                Cargo cargoEnum;
                try {
                    cargoEnum = Cargo.valueOf(cargoString.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    AlertUtils.showError("Erro de Cargo", "O cargo selecionado é inválido.");
                    return;
                }

                BigDecimal salario;
                try {
                    salario = new BigDecimal(salarioField.getText().trim().replace(",", "."));
                } catch (NumberFormatException ex) {
                    AlertUtils.showError("Erro de Salário", "O salário deve ser um valor numérico válido.");
                    markFieldAsError(salarioField);
                    return;
                }

                LocalDate dataNascimento = dataNascimentoDatePicker.getValue() != null
                        ? dataNascimentoDatePicker.getValue()
                        : null;
                LocalDate dataAdmissao = dataAdmissaoPicker.getValue() != null
                        ? dataAdmissaoPicker.getValue()
                        : null;

                String senha = senhaField.getText();
                boolean ativo = ativoCheck.isSelected();

                try {
                    FuncionarioCreateDto funcionarioCreateDto = new FuncionarioCreateDto(cpf, nome, dataNascimento, telefone, email, endereco, dataAdmissao, cargoEnum, salario, senha, ativo);
                    FuncionarioResponseDto funcionarioResponseDto = funcionarioController.salvarFuncionario(funcionarioCreateDto);
                    if (funcionarioResponseDto != null) {
                        //Impressora.imprimirCodigoFuncionario(funcionarioResponseDto.getCodigoVerificador());
                        AlertUtils.showSuccess("Sucesso!", "Operador " + nome + " cadastrado com sucesso.");
                        dialogStage.close();
                        if (updateTableRunnable != null) {
                            updateTableRunnable.run();
                        }
                    }
                } catch (Exception ex) {
                    AlertUtils.showError("Erro de Execução",
                            "Ocorreu um erro ao salvar o funcionário: " + ex.getMessage());
                }
            }
        });

        buttonBox.getChildren().addAll(btnCancel, btnSave);
        return buttonBox;
    }

    /**
     * Reseta os estilos de todos os campos de entrada para o padrão base.
     */
    private void resetFieldStyles() {
        nomeField.getStyleClass().add("field-base");
        cpfField.getStyleClass().add("field-base");
        telefoneField.getStyleClass().add("field-base");
        emailField.getStyleClass().add("field-base");
        enderecoField.getStyleClass().add("field-base");
        cargoCombo.getStyleClass().add("field-base");
        salarioField.getStyleClass().add("field-base");
        dataNascimentoDatePicker.getStyleClass().add("field-base");
        dataAdmissaoPicker.getStyleClass().add("field-base");
        senhaField.getStyleClass().add("field-base");
    }

    /**
     * Valida todos os campos do formulário e exibe mensagens de erro.
     *
     * @return true se todos os campos são válidos, false caso contrário.
     */
    private boolean validateAllFields() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        // 1. VALIDAÇÃO DE CAMPOS DE TEXTO E SELEÇÃO (OBRIGATÓRIOS)
        // NOME (OBRIGATÓRIO)
        if (nomeField.getText().trim().isEmpty()) {
            isValid = false;
            markFieldAsError(nomeField);
            errorMessage.append("• O campo 'Nome Completo' é obrigatório.\n");
        }

        // CPF (OBRIGATÓRIO)
        String cpf = cpfField.getText().trim();
        String cleanedCpf = cpf.replaceAll("[^0-9]", "");
        if (!InputUtils.validateCPF(cleanedCpf)) {
            isValid = false;
            markFieldAsError(cpfField);
            errorMessage.append("• CPF inválido. Verifique o formato e os dígitos.\n");
        }

        // TELEFONE (OBRIGATÓRIO)
        String telefone = telefoneField.getText().trim();
        String cleanedTelefone = telefone.replaceAll("[^0-9]", "");
        if (!InputUtils.validatePhone(cleanedTelefone)) {
            isValid = false;
            markFieldAsError(telefoneField);
            errorMessage.append("• Telefone inválido. Deve conter 11 dígitos numéricos.\n");
        }

        // CARGO (OBRIGATÓRIO)
        if (cargoCombo.getValue() == null || cargoCombo.getValue().isEmpty()) {
            isValid = false;
            markFieldAsError(cargoCombo);
            errorMessage.append("• Selecione um 'Cargo' para o funcionário.\n");
        }

        // SENHA (OBRIGATÓRIO)
        String senha = senhaField.getText();
        if (!InputUtils.validatePassword(senha, 6)) {
            isValid = false;
            markFieldAsError(senhaField);
            errorMessage.append("• Senha inválida. Deve ter pelo menos 6 caracteres.\n");
        }

        // --------------------------------------------------------------------------------
        // 2. VALIDAÇÃO DE CAMPOS NÃO OBRIGATÓRIOS (Email, Endereço, Data de Nascimento)
        // --------------------------------------------------------------------------------

        // EMAIL (NÃO OBRIGATÓRIO)
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !InputUtils.validateEmail(email)) {
            isValid = false;
            markFieldAsError(emailField);
            errorMessage.append("• Email inválido. Verifique o formato (ex: nome@dominio.com).\n");
        }

        // ENDEREÇO (NÃO OBRIGATÓRIO)
        if (enderecoField.getText().trim().isEmpty()) {
            markFieldAsError(enderecoField);
        }
        // DATA DE NASCIMENTO (NÃO OBRIGATÓRIO)
        if (dataNascimentoDatePicker.getValue() != null) {
            if (dataNascimentoDatePicker.getValue().isAfter(LocalDate.now().minusYears(16))) {
                isValid = false;
                markFieldAsError(dataNascimentoDatePicker);
                errorMessage.append(
                        "• Se preenchida, a 'Data de Nascimento' indica que o funcionário deve ter pelo menos 16 anos.\n");
            }
        }

        // DATA DE ADMISSAO (NÃO OBRIGATÓRIO)
        if (dataAdmissaoPicker.getValue() != null) {
            if (dataAdmissaoPicker.getValue().isAfter(LocalDate.now())) {
                isValid = false;
                markFieldAsError(dataAdmissaoPicker);
                errorMessage.append("• A 'Data de Admissão' não pode ser no futuro.\n");
            }
        }

        // SALÁRIO (NÃO OBRIGATÓRIO)
        String salarioText = salarioField.getText().trim();
        if (!salarioText.isEmpty()) {
            if (!validateSalary(salarioField)) {
                isValid = false;
                markFieldAsError(salarioField);
                errorMessage.append("• Salário inválido. Digite um valor numérico válido.\n");
            }
        }

        // --------------------------------------------------------------------------------
        if (!isValid) {
            AlertUtils.showError("Erros de Validação",
                    "Por favor, corrija os seguintes problemas:\n" + errorMessage.toString());
        }
        return isValid;
    }

    /**
     * Marca um campo como erro, aplicando o estilo de erro.
     *
     * @param control O campo a ser marcado.
     */
    private void markFieldAsError(Control control) {
        control.getStyleClass().remove("field-error");
        control.getStyleClass().add("field-error");
    }

    /**
     * Valida se um campo de texto não está vazio ou contém apenas espaços.
     *
     * @param field     O TextField a ser validado.
     * @param fieldName O nome do campo para mensagens de erro.
     * @return true se o campo não está vazio, false caso contrário.
     */
    private boolean validateNonEmptyText(TextField field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            markFieldAsError(field);
            return false;
        }
        return true;
    }

    /**
     * Valida se o campo de salário contém um número positivo válido.
     *
     * @param field O TextField do salário.
     * @return true se o salário é válido, false caso contrário.
     */
    private boolean validateSalary(TextField field) {
        String salarioText = field.getText().trim().replace(",", ".");
        if (salarioText.isEmpty()) {
            markFieldAsError(field);
            return false;
        }
        try {
            double salario = Double.parseDouble(salarioText);
            if (salario <= 0) {
                markFieldAsError(field);
                return false;
            }
        } catch (NumberFormatException e) {
            markFieldAsError(field);
            return false;
        }
        return true;
    }

    /**
     * Metodo principal para exibir a dialog de cadastro.
     *
     * @param updateTableRunnable Runnable a ser executada após o cadastro
     *                            bem-sucedido.
     */
    public void show(Runnable updateTableRunnable) {
        Stage dialog = new Stage();
        dialog.setTitle("Cadastrar Funcionário");
        dialog.initModality(Modality.APPLICATION_MODAL);
        // Define o Stage proprietário (owner) da dialog
        if (this.ownerStage != null) {
            dialog.initOwner(this.ownerStage);
        }
        dialog.setResizable(false);
        dialog.setWidth(950);
        dialog.setHeight(750);
        dialog.centerOnScreen();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f7f6;");

        root.setTop(createTitle());
        GridPane form = createForm();
        root.setCenter(form);

        HBox buttonBox = createButtonsSection(dialog, updateTableRunnable);
        root.setBottom(buttonBox);

        BorderPane.setMargin(form, new Insets(10, 20, 0, 20));
        BorderPane.setMargin(buttonBox, new Insets(20, 20, 20, 20));

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}