package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.controller.FuncionarioController;
import br.com.mercadinhoprovidence.dto.FuncionarioTableDto;
import br.com.mercadinhoprovidence.dto.LoginResponseDto;
import br.com.mercadinhoprovidence.model.Funcionario;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.FormatUtils;
import br.com.mercadinhoprovidence.view.component.TitleComponents;
import br.com.mercadinhoprovidence.view.dialogs.CadastroFuncionarioDialog;
import br.com.mercadinhoprovidence.view.dialogs.ConfirmarDialog;
import br.com.mercadinhoprovidence.view.dialogs.EditarFuncionarioDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class FuncionariosView {

    private MainApplication mainApplication;
    private LoginResponseDto funcionarioLogado;
    private StackPane viewPane;

    private ObservableList<FuncionarioTableDto> listaFuncionariosNaTabela;
    private TableView<FuncionarioTableDto> employeesTable;
    private Label totalFuncionariosLabel;
    private TextField searchBar;

    private final FuncionarioController funcionarioController;

    /**
     * Construtor da FuncionariosView.
     *
     * @param mainApplication   Instância principal da aplicação.
     * @param funcionarioLogado Funcionário atualmente logado (se necessário para
     *                          permissões, etc.).
     */
    public FuncionariosView(MainApplication mainApplication, LoginResponseDto funcionarioLogado) {
        this.mainApplication = mainApplication;
        this.funcionarioLogado = funcionarioLogado;
        this.funcionarioController = new FuncionarioController();
        this.viewPane = initializeView();
    }

    /**
     * Retorna a visualização completa para a seção de Funcionários.
     * Este metodo agora é público e não estático, para ser chamado pela
     * MainApplication.
     *
     * @return Um StackPane contendo todo o conteudo.
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
        pane.getStylesheets().add(getClass().getResource("/styles/TelaFuncionario.css").toExternalForm());

        // --- VBox PRINCIPAL PARA ORGANIZAR TUDO VERTICALMENTE ---
        VBox mainContentVBox = new VBox(10);
        mainContentVBox.setPadding(new Insets(20, 30, 20, 30));
        mainContentVBox.getChildren().addAll(
                TitleComponents.crateHeadBorderPane("Gestão de Funcionários"),
                createOptionsSection(),
                createEmployeesTable());

        VBox.setVgrow(this.employeesTable, Priority.ALWAYS);

        pane.getChildren().add(mainContentVBox);
        return pane;
    }

    /**
     * Metodo auxiliar generico para criar uma TableColumn.
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

        // Aplica alinhamento
        if (alignment == Pos.CENTER) {
            column.getStyleClass().add("column-center");
        } else if (alignment == Pos.CENTER_LEFT) {
            column.getStyleClass().add("column-left");
        } else if (alignment == Pos.CENTER_RIGHT) {
            column.getStyleClass().add("column-right");
        }

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
     * cadastrar funcionário.
     */
    private BorderPane createOptionsSection() {
        // --- CONTADOR DE FUNCIONÁRIOS ---
        Label labelTotal = new Label("Total de Funcionários:");
        labelTotal.getStyleClass().add("total-label");
        this.totalFuncionariosLabel = new Label("0");
        this.totalFuncionariosLabel.getStyleClass().add("total-label");

        HBox totalBox = new HBox(5, labelTotal, this.totalFuncionariosLabel);
        totalBox.setAlignment(Pos.CENTER_LEFT);

        // --- BARRA DE PESQUISA ---
        searchBar = new TextField();
        searchBar.setPromptText("Pesquisar por CPF");
        searchBar.setPrefWidth(250);
        searchBar.getStyleClass().add("search-field");

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchBar.setText(oldValue);
            }
        });

        Button searchButton = new Button("Pesquisar");
        searchButton.setPrefHeight(30);
        searchButton.getStyleClass().add("search-button");

        searchButton.setOnAction(e -> {
            String cpfPesquisa = searchBar.getText().trim();
            if (cpfPesquisa.isEmpty()) {
                AlertUtils.showWarning("Pesquisa Vazia", "Por favor, digite o CPF do funcionário para pesquisar.");
                searchBar.requestFocus();
                return;
            }

            String cleanedCpf = cpfPesquisa.replaceAll("[^0-9]", "");
            FuncionarioTableDto funcionarioEncontrado = funcionarioController.buscarPorCpf(cleanedCpf);

            if (funcionarioEncontrado != null) {
                selecionarFuncionarioNaTabela(funcionarioEncontrado);
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
        registerButton.getStyleClass().add("button-green");

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
    private TableView<FuncionarioTableDto> createEmployeesTable() {
        employeesTable = new TableView<>();
        employeesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeesTable.setPrefHeight(400);

        // --- DEFINIÇÃO DAS COLUNAS ---
        TableColumn<FuncionarioTableDto, Integer> colId = createColumn("ID", "idFuncionario", 0, Pos.CENTER, null);
        TableColumn<FuncionarioTableDto, Integer> colCodigoFun = createColumn("Código Verificador", "codigoFuncionario", 0,
                Pos.CENTER, null);
        TableColumn<FuncionarioTableDto, String> colNome = createColumn("Nome", "nome", 200, Pos.CENTER, null);
        TableColumn<FuncionarioTableDto, String> colCPF = createColumn("CPF", "cpf", 120, Pos.CENTER, FormatUtils::formatarCpf);
        TableColumn<FuncionarioTableDto, String> colCargo = createColumn("Cargo", "cargo", 100, Pos.CENTER, null);
        TableColumn<FuncionarioTableDto, BigDecimal> colSalario = createColumn("Salário (R$)", "salario", 100, Pos.CENTER,
                FormatUtils::formatCurrency);
        TableColumn<FuncionarioTableDto, LocalDate> colDataContratacao =
                createColumn("Contratação", "dataAdmissao", 120, Pos.CENTER,
                        FormatUtils::formatDate);
        TableColumn<FuncionarioTableDto, Boolean> colAtivo = createColumn("Ativo", "ativo", 60, Pos.CENTER,
                FormatUtils::formatBoolean);

        // Coluna de Ações
        TableColumn<FuncionarioTableDto, Void> colActions = new TableColumn<>("Ações");
        colActions.setPrefWidth(150);
        colActions.setResizable(false);
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Editar");
            private final Button btnDelete = new Button("Excluir");
            private final HBox actionButtons = new HBox(10, btnEdit, btnDelete);

            {
                btnEdit.getStyleClass().add("btn-table-edit");
                btnDelete.getStyleClass().add("btn-table-delete");

                actionButtons.setAlignment(Pos.CENTER);

                // Ações dos botões
                btnEdit.setOnAction(event -> {
                    FuncionarioTableDto funcionario = getTableView().getItems().get(getIndex());
                    EditarFuncionarioDialog.show(mainApplication.getPrimaryStage(), funcionario.getIdFuncionario(), () -> {
                        refreshTableData();
                    });
                });

                btnDelete.setOnAction(event -> {
                    FuncionarioTableDto funcionarioTableDto = getTableView().getItems().get(getIndex());
                    if (Objects.equals(funcionarioLogado.getCodigoVerificador(), funcionarioTableDto.getCodigoFuncionario())) {
                        AlertUtils.showInfo("Ação inválida", "Não é possível deletar o funcionário que está logado");
                    }
                    ConfirmarDialog confirmDialog = new ConfirmarDialog();
                    boolean confirmed = confirmDialog.show(mainApplication.getPrimaryStage(), funcionarioTableDto.getNome(), "Funcinonário");
                    if (confirmed) {
                        try {
                            boolean success = funcionarioController.deletarFuncionario(funcionarioTableDto.getIdFuncionario());
                            if (success) {
                                AlertUtils.showSuccess("Excluído!", "Funcionário excluído com sucesso.");
                                refreshTableData();
                            }
                        } catch (Exception e) {
                            AlertUtils.showError("Erro na Exclusão",
                                    "Ocorreu um erro ao tentar excluir o funcionário:\n" + e.getMessage());
                        }
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
            List<FuncionarioTableDto> allEmployees = funcionarioController.listarTodos();
            listaFuncionariosNaTabela = FXCollections.observableArrayList(allEmployees);
            employeesTable.setItems(listaFuncionariosNaTabela);
            refreshTotalFuncionariosCount();
        } catch (Exception e) {
            AlertUtils.showError("Erro de Carga",
                    "Não foi possível carregar a lista de funcionários:\n" + e.getMessage());
        }
    }

    /**
     * Atualiza o Label que mostra a quantidade total de funcionários.
     */
    private void refreshTotalFuncionariosCount() {
        this.totalFuncionariosLabel.setText(String.valueOf(listaFuncionariosNaTabela.size()));
    }

   /**
    * Seleciona um funcionário na tabela
    *
    * @param  funcionario que vem do resultado do banco de dados
    */
    private void selecionarFuncionarioNaTabela(FuncionarioTableDto funcionario) {
        boolean foundAndSelected = false;

        for (FuncionarioTableDto f : this.listaFuncionariosNaTabela) {
            // Analisar o uso de == e Objects.equals
            if (Objects.equals(f.getIdFuncionario(), funcionario.getIdFuncionario())) {
                employeesTable.getSelectionModel().select(f);
                employeesTable.scrollTo(f);
                foundAndSelected = true;
                break;
            }
        }

        if (!foundAndSelected) {
            refreshTableData();
            for (FuncionarioTableDto f : this.listaFuncionariosNaTabela) {
                if (Objects.equals(f.getIdFuncionario(), funcionario.getIdFuncionario())) {
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
    }
}