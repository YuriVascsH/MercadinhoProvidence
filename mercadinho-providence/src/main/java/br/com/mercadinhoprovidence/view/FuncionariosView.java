package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.controller.FuncionarioController;
import br.com.mercadinhoprovidence.model.Funcionario;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.FormatUtils;
import br.com.mercadinhoprovidence.view.components.ScreenTitle;
import br.com.mercadinhoprovidence.view.dialogs.CadastroFuncionarioDialog;
import br.com.mercadinhoprovidence.view.dialogs.ConfirmarDialogFuncionario;
import br.com.mercadinhoprovidence.view.dialogs.EditarFuncionarioDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class FuncionariosView {

    private MainApplication mainApplication;
    private Funcionario funcionarioLogado;
    private StackPane viewPane;

    private ObservableList<Funcionario> listaFuncionariosNaTabela;
    private TableView<Funcionario> employeesTable;
    private Label totalFuncionariosLabel;
    private TextField searchBar;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final FuncionarioController funcionarioController;

    // Constantes para estilos de botões
    private static final String BUTTON_BASE_STYLE_GREEN = "-fx-background-color: #28a745;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 6 14 6 14;" +
            "-fx-font-size: 16px;";
    private static final String BUTTON_HOVER_STYLE_GREEN = "-fx-background-color: #4bc56b;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 6 14 6 14;" +
            "-fx-font-size: 16px;";

    private static final String BUTTON_TABLE_EDIT_STYLE = "-fx-background-color: #4CAF50;-fx-text-fill: white;-fx-font-weight: bold;";
    private static final String BUTTON_TABLE_EDIT_HOVER_STYLE = "-fx-background-color: #388E3C;-fx-text-fill: white;-fx-font-weight: bold;";
    private static final String BUTTON_TABLE_DELETE_STYLE = "-fx-background-color: #f44336;-fx-text-fill: white;-fx-font-weight: bold;";
    private static final String BUTTON_TABLE_DELETE_HOVER_STYLE = "-fx-background-color: #D32F2F;-fx-text-fill: white;-fx-font-weight: bold;";

    // Estilos para a barra de pesquisa e botão de pesquisar (copiado da
    // EstoqueView)
    private static final String SEARCH_FIELD_STYLE = "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #4A90E2;" + // Borda azul
            "-fx-border-width: 1.5px;" +
            "-fx-padding: 6 10 6 10;" +
            "-fx-font-size: 14px;";

    private static final String SEARCH_BUTTON_BASE_STYLE = "-fx-background-color: #3478f6;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 6 14 6 14;" +
            "-fx-font-size: 16px;";

    private static final String SEARCH_BUTTON_HOVER_STYLE = "-fx-background-color: #5599ff;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 6 14 6 14;" +
            "-fx-font-size: 16px;";

    /**
     * Construtor da FuncionariosView.
     *
     * @param mainApplication   Instância principal da aplicação.
     * @param funcionarioLogado Funcionário atualmente logado (se necessário para
     *                          permissões, etc.).
     */
    public FuncionariosView(MainApplication mainApplication, Funcionario funcionarioLogado) {
        this.mainApplication = mainApplication;
        this.funcionarioLogado = funcionarioLogado;
        this.funcionarioController = new FuncionarioController();
        this.viewPane = initializeView();
    }

    /**
     * Retorna a visualização completa para a seção de Funcionários.
     * Este método agora é público e não estático, para ser chamado pela
     * MainApplication.
     *
     * @return Um StackPane contendo todo o conteúdo.
     */
    public StackPane getView() {
        return this.viewPane;
    }

    /**
     * Inicializa todos os componentes da interface do usuário para a tela de
     * Funcionários.
     *
     * @return O StackPane raiz contendo toda a interface.
     */
    private StackPane initializeView() {
        StackPane pane = new StackPane();

        String selectionCss = ".table-row-cell:selected {" +
                "    -fx-background-color: #3478f6;" +
                "    -fx-text-fill: white;" +
                "}" +
                ".table-row-cell:selected .text {" +
                "    -fx-fill: white;" +
                "}" +
                // --- Adicione esta nova regra para fixar a cor dos botões ---
                ".button .text {" +
                "    -fx-fill: white;" +
                "}" +
                ".button:hover .text {" +
                "    -fx-fill: white;" +
                "}";
        pane.getStylesheets().add("data:text/css," + selectionCss);

        // --- VBox PRINCIPAL PARA ORGANIZAR TUDO VERTICALMENTE ---
        VBox mainContentVBox = new VBox(10);
        mainContentVBox.setPadding(new Insets(20, 30, 20, 30));
        mainContentVBox.getChildren().addAll(
                ScreenTitle.crateHeadBorderPane("Gestão de Funcionários"),
                createOptionsSection(),
                createEmployeesTable());

        VBox.setVgrow(this.employeesTable, Priority.ALWAYS); // Faz a tabela crescer

        pane.getChildren().add(mainContentVBox);
        return pane;
    }

    /**
     * Método auxiliar genérico para criar uma TableColumn.
     *
     * @param <S>           O tipo do objeto na TableView (ex: Funcionario).
     * @param <T>           O tipo do valor da célula na coluna (ex: String,
     *                      Integer).
     * @param title         O título exibido no cabeçalho da coluna.
     * @param propertyName  O nome da propriedade do objeto S para ser lido pela
     *                      PropertyValueFactory.
     * @param prefWidth     A largura preferencial da coluna (opcional, use 0 para
     *                      não definir).
     * @param alignment     O alinhamento do texto na célula (opcional, use null
     *                      para padrão).
     * @param cellFormatter Uma função para formatar o valor da célula (opcional,
     *                      use null para formato padrão).
     * @return Uma TableColumn configurada.
     */
    private <S, T> TableColumn<S, T> createColumn(String title, String propertyName, double prefWidth, Pos alignment,
            Function<T, String> cellFormatter) {
        TableColumn<S, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));

        // Remova completamente este bloco de código
        // if (prefWidth > 0) {
        // column.setPrefWidth(prefWidth);
        // }

        // Aplica alinhamento
        column.setStyle("-fx-alignment: " + (alignment != null ? alignment.name() : Pos.CENTER.name()) + ";");

        if (cellFormatter != null) {
            column.setCellFactory(col -> new TableCell<S, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(cellFormatter.apply(item));
                    }
                }
            });
        }
        return column;
    }

    /**
     * Cria e configura a parte de opções (Contador, Pesquisar e Cadastrar).
     *
     * @return Um BorderPane contendo barra de pesquisa de funcionário e botão de
     *         cadastrar funcionário.
     */
    private BorderPane createOptionsSection() {
        // --- CONTADOR DE FUNCIONÁRIOS ---
        Label labelTotal = new Label("Total de Funcionários:");
        labelTotal.setStyle("-fx-font-size: 20px;");
        this.totalFuncionariosLabel = new Label("0");
        this.totalFuncionariosLabel.setStyle("-fx-font-size: 20px;");

        HBox totalBox = new HBox(5, labelTotal, this.totalFuncionariosLabel);
        totalBox.setAlignment(Pos.CENTER_LEFT);

        // --- BARRA DE PESQUISA ---
        searchBar = new TextField();
        searchBar.setPromptText("Pesquisar por CPF");
        searchBar.setPrefWidth(250);
        searchBar.setStyle(SEARCH_FIELD_STYLE);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchBar.setText(oldValue);
            }
        });

        Button searchButton = new Button("Pesquisar");
        searchButton.setPrefHeight(30);
        searchButton.setStyle(SEARCH_BUTTON_BASE_STYLE);

        searchButton.setOnMouseEntered(e -> {
            searchButton.setStyle(SEARCH_BUTTON_HOVER_STYLE);
            searchButton.setCursor(Cursor.HAND);
        });
        searchButton.setOnMouseExited(e -> {
            searchButton.setStyle(SEARCH_BUTTON_BASE_STYLE);
            searchButton.setCursor(Cursor.DEFAULT);
        });

        searchButton.setOnAction(e -> {
            String cpfPesquisa = searchBar.getText().trim();
            if (cpfPesquisa.isEmpty()) {
                AlertUtils.showWarning("Pesquisa Vazia", "Por favor, digite o CPF do funcionário para pesquisar.");
                searchBar.requestFocus();
                return;
            }

            String cleanedCpf = cpfPesquisa.replaceAll("[^0-9]", "");

            Funcionario funcionarioEncontrado = funcionarioController.buscarPorCpf(cleanedCpf);

            if (funcionarioEncontrado != null) {
                final Funcionario finalFuncionario = funcionarioEncontrado;
                boolean foundAndSelected = false;

                for (Funcionario f : this.listaFuncionariosNaTabela) {

                    if (f.getIdFuncionario() == finalFuncionario.getIdFuncionario()) {
                        employeesTable.getSelectionModel().select(f);
                        employeesTable.scrollTo(f);
                        foundAndSelected = true;
                        System.out.println("Funcionário encontrado e selecionado na tabela: " + f.getNome());
                        break;
                    }
                }

                if (!foundAndSelected) {

                    System.out.println(
                            "Funcionário encontrado no BD, mas não visível na tabela. Recarregando e tentando novamente.");
                    refreshTableData();

                    for (Funcionario f : this.listaFuncionariosNaTabela) {
                        if (f.getIdFuncionario() == finalFuncionario.getIdFuncionario()) {
                            employeesTable.getSelectionModel().select(f);
                            employeesTable.scrollTo(f);
                            foundAndSelected = true;
                            break;
                        }
                    }
                }

                if (!foundAndSelected) {
                    AlertUtils.showWarning("Problema ao Selecionar",
                            "Funcionário encontrado, mas não foi possível selecioná-lo na tabela. Tente recarregar a tela.");
                }

                searchBar.clear();
            } else {

                AlertUtils.showInfo("Funcionário Não Encontrado", "Nenhum funcionário corresponde ao CPF informado.");
                searchBar.clear();
                refreshTableData();
            }
            searchBar.getParent().requestFocus();

        });

        searchBar.setOnAction(searchButton.getOnAction());

        HBox searchBox = new HBox(10, searchBar, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // --- BOTÃO DE CADASTRAR ---
        Button registerButton = new Button("Cadastrar Funcionário");
        registerButton.setPrefHeight(30);
        registerButton.setStyle(BUTTON_BASE_STYLE_GREEN);

        registerButton.setOnMouseEntered(e -> {
            registerButton.setStyle(BUTTON_HOVER_STYLE_GREEN);
            registerButton.setCursor(Cursor.HAND);
        });

        registerButton.setOnMouseExited(e -> {
            registerButton.setStyle(BUTTON_BASE_STYLE_GREEN);
            registerButton.setCursor(Cursor.DEFAULT);
        });

        // Ação do botão de cadastro
        registerButton.setOnAction(event -> {
            CadastroFuncionarioDialog cadastroDialog = new CadastroFuncionarioDialog(mainApplication.getPrimaryStage());
            cadastroDialog.show(() -> {
                refreshTableData();
            });
        });

        // --- CONTAINER PRINCIPAL PARA OPÇÕES (BorderPane) ---
        BorderPane optionsPane = new BorderPane();
        optionsPane.setLeft(totalBox);
        optionsPane.setCenter(searchBox);
        optionsPane.setRight(registerButton);
        optionsPane.setPadding(new Insets(10, 0, 10, 0));
        return optionsPane;
    }

    /**
     * Cria e configura a tabela de funcionários.
     *
     * @return Uma tabela contendo as informações dos funcionários.
     */
    private TableView<Funcionario> createEmployeesTable() {
        employeesTable = new TableView<>();
        employeesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeesTable.setPrefHeight(400);

        // --- DEFINIÇÃO DAS COLUNAS ---
        TableColumn<Funcionario, Integer> colId = createColumn("ID", "idFuncionario", 0, Pos.CENTER, null);
        TableColumn<Funcionario, Integer> colCodigoFun = createColumn("Código Verificador", "codigoVerificador", 0,
                Pos.CENTER, null);
        TableColumn<Funcionario, String> colNome = createColumn("Nome", "nome", 200, Pos.CENTER, null);
        TableColumn<Funcionario, String> colCPF = createColumn("CPF", "cpf", 120, Pos.CENTER, FormatUtils::formatarCpf);
        TableColumn<Funcionario, String> colCargo = createColumn("Cargo", "cargo", 100, Pos.CENTER, null);
        TableColumn<Funcionario, BigDecimal> colSalario = createColumn("Salário (R$)", "salario", 100, Pos.CENTER,
                this::formatCurrency);
        // TableColumn<Funcionario, LocalDate> colDataContratacao =
        // createColumn("Contratação", "DataAdmissao", 120, Pos.CENTER,
        // this::formatDate);
        TableColumn<Funcionario, Boolean> colAtivo = createColumn("Ativo", "ativo", 60, Pos.CENTER,
                this::formatBoolean);

        // Coluna de Ações
        TableColumn<Funcionario, Void> colActions = new TableColumn<>("Ações");
        colActions.setPrefWidth(150);
        colActions.setResizable(false);
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Editar");
            private final Button btnDelete = new Button("Excluir");
            private final HBox actionButtons = new HBox(10, btnEdit, btnDelete);

            {
                btnEdit.setStyle(BUTTON_TABLE_EDIT_STYLE);
                btnEdit.setOnMouseEntered(e -> btnEdit.setStyle(BUTTON_TABLE_EDIT_HOVER_STYLE));
                btnEdit.setOnMouseExited(e -> btnEdit.setStyle(BUTTON_TABLE_EDIT_STYLE));
                btnEdit.setCursor(Cursor.HAND);

                btnDelete.setStyle(BUTTON_TABLE_DELETE_STYLE);
                btnDelete.setOnMouseEntered(e -> btnDelete.setStyle(BUTTON_TABLE_DELETE_HOVER_STYLE));
                btnDelete.setOnMouseExited(e -> btnDelete.setStyle(BUTTON_TABLE_DELETE_STYLE));
                btnDelete.setCursor(Cursor.HAND);

                actionButtons.setAlignment(Pos.CENTER);

                // Ações dos botões
                btnEdit.setOnAction(event -> {
                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                    EditarFuncionarioDialog.show(mainApplication.getPrimaryStage(), funcionario, () -> {
                        refreshTableData();
                    });
                });

                btnDelete.setOnAction(event -> {
                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                    ConfirmarDialogFuncionario confirmDialog = new ConfirmarDialogFuncionario();
                    boolean confirmed = confirmDialog.show(mainApplication.getPrimaryStage(), funcionario.getNome());
                    if (confirmed) {
                        try {
                            boolean success = funcionarioController.deletar(funcionario.getIdFuncionario());

                            if (success) {
                                AlertUtils.showSuccess("Excluído!", "Funcionário excluído com sucesso.");
                                refreshTableData();
                            } else {
                                AlertUtils.showError("Erro",
                                        "Não foi possível excluir o funcionário. Verifique se há dependências ou consulte o log.");
                            }
                        } catch (Exception e) {
                            AlertUtils.showError("Erro na Exclusão",
                                    "Ocorreu um erro ao tentar excluir o funcionário:\n" + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Exclusão de funcionário cancelada pelo usuário.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionButtons);
            }
        });

        // Adiciona as colunas à tabela
        employeesTable.getColumns().addAll(colId, colCodigoFun, colNome, colCPF, colCargo, colSalario, colAtivo,
                colActions);

        // Carrega os dados iniciais
        refreshTableData();

        return employeesTable;
    }

    /**
     * Carrega ou recarrega os dados da tabela de funcionários.
     */
    private void refreshTableData() {
        try {
            List<Funcionario> allEmployees = funcionarioController.listarTodos();
            listaFuncionariosNaTabela = FXCollections.observableArrayList(allEmployees);
            employeesTable.setItems(listaFuncionariosNaTabela);
            refreshTotalFuncionariosCount();
        } catch (Exception e) {
            AlertUtils.showError("Erro de Carga",
                    "Não foi possível carregar a lista de funcionários:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Atualiza o Label que mostra a quantidade total de funcionários.
     */
    private void refreshTotalFuncionariosCount() {
        this.totalFuncionariosLabel.setText(String.valueOf(listaFuncionariosNaTabela.size()));
    }

    /**
     * Formata um valor BigDecimal como moeda brasileira (R$).
     *
     * @param unformattedSalary Salário sem formatação.
     * @return Retorna o salário formatado como moeda.
     */
    private String formatCurrency(BigDecimal unformattedSalary) {
        if (unformattedSalary == null) {
            return currencyFormat.format(BigDecimal.ZERO);
        }
        return currencyFormat.format(unformattedSalary);
    }

    /**
     * Formata um LocalDate para String no formato "dd/MM/yyyy".
     *
     * @param date Data a ser formatada.
     * @return String formatada da data.
     */
    private String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(dateFormatter);
    }

    /**
     * Formata um booleano para "Sim" ou "Não".
     *
     * @param value Valor booleano.
     * @return "Sim" se true, "Não" se false.
     */
    private String formatBoolean(Boolean value) {
        if (value == null) {
            return "Não informado";
        }
        return value ? "Sim" : "Não";
    }
}