package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.controller.EstoqueController;
import br.com.mercadinhoprovidence.controller.ProdutoController;
import br.com.mercadinhoprovidence.model.Funcionario;
import br.com.mercadinhoprovidence.model.Produto;
import br.com.mercadinhoprovidence.model.enums.Categoria;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.view.components.ScreenTitle;
import br.com.mercadinhoprovidence.view.dialogs.CadastroProdutosDialog;
import br.com.mercadinhoprovidence.view.dialogs.ConfirmacaoDialog;
import br.com.mercadinhoprovidence.view.dialogs.EditarProdutoDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class EstoqueView {

    private MainApplication mainApplication;
    private Funcionario funcionarioLogado;
    private StackPane viewPane;
    private ProdutoController produtoController;
    private EstoqueController estoqueController;

    private ObservableList<Produto> listaProdutosNaTabela;
    private TableView<Produto> tabelaProdutos;
    private Label bancoQtdEstoque;

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EstoqueView(MainApplication mainApplication, Funcionario funcionarioLogado) {
        this.mainApplication = mainApplication;
        this.funcionarioLogado = funcionarioLogado;
        this.produtoController = new ProdutoController();
        this.estoqueController = new EstoqueController();
        this.viewPane = initializeView();
    }

    public StackPane getView() {
        return this.viewPane;
    }

    public StackPane initializeView() {
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

        Label qtdEstoque = new Label("Quantidade Estoque:");
        qtdEstoque.setStyle("-fx-font-size: 20px;");

        this.bancoQtdEstoque = new Label(String.valueOf(estoqueController.contarProdutosNoEstoque(1)));
        this.bancoQtdEstoque.setStyle("-fx-font-size: 20px;");

        HBox boxQtdEstoque = new HBox(5, qtdEstoque, this.bancoQtdEstoque);
        boxQtdEstoque.setAlignment(Pos.CENTER_LEFT);

        /*** BARRA DE PESQUISA ***/
        TextField barraPesquisa = new TextField();
        barraPesquisa.setPromptText("Pesquisar por Código de Barras");
        barraPesquisa.setPrefWidth(250);
        barraPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                barraPesquisa.setText(oldValue);
            }
        });

        barraPesquisa.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: #bbb;" +
                        "-fx-border-width: 1.5px;" +
                        "-fx-padding: 6 10 6 10;" +
                        "-fx-font-size: 14px;");

        Button pesquisarPorButton = new Button("Pesquisar");
        pesquisarPorButton.setPrefHeight(30);
        pesquisarPorButton.setStyle(
                "-fx-background-color: #3478f6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 6 14 6 14;" +
                        "-fx-font-size: 16px;");

        pesquisarPorButton.setOnMouseEntered(e -> {
            pesquisarPorButton.setStyle(
                    "-fx-background-color: #5599ff;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 6 14 6 14;" +
                            "-fx-font-size: 16px;");
            pesquisarPorButton.setCursor(Cursor.HAND);
        });

        pesquisarPorButton.setOnMouseExited(e -> {
            pesquisarPorButton.setStyle(
                    "-fx-background-color: #3478f6;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 6 14 6 14;" +
                            "-fx-font-size: 16px;");
            pesquisarPorButton.setCursor(Cursor.DEFAULT);
        });

        pesquisarPorButton.setOnAction(e -> {
            String codigoInseridoPesquisa = barraPesquisa.getText().trim();
            if (codigoInseridoPesquisa.isEmpty()) {
                AlertUtils.showInfo("Pesquisa Vazia", "Por favor, digite o Código de Barras para pesquisar.");
                barraPesquisa.clear();
                return;
            }

            Produto produtoEncontrado = this.produtoController.buscarPorCodigo(codigoInseridoPesquisa);

            if (produtoEncontrado != null) {
                final Produto finalProduto = produtoEncontrado;
                boolean foundInTable = false;

                for (Produto p : this.listaProdutosNaTabela) {
                    if (p.getIdProduto() == finalProduto.getIdProduto()) {
                        tabelaProdutos.getSelectionModel().select(p);
                        tabelaProdutos.scrollTo(p);
                        foundInTable = true;
                        System.out.println("Produto encontrado e selecionado na tabela: " + p.getNome());
                        break;
                    }
                }

                if (!foundInTable) {
                    System.out.println(
                            "Produto encontrado no BD, mas não visível na tabela. Recarregando e tentando novamente.");
                    atualizarTabelaCompleta();
                    for (Produto p : this.listaProdutosNaTabela) {
                        if (p.getIdProduto() == finalProduto.getIdProduto()) {
                            tabelaProdutos.getSelectionModel().select(p);
                            tabelaProdutos.scrollTo(p);
                            break;
                        }
                    }
                }
                barraPesquisa.clear();
            } else {
                AlertUtils.showInfo("Produto Não Encontrado",
                        "Nenhum produto corresponde ao Código de Barras informado.");
                barraPesquisa.clear();
            }
            barraPesquisa.getParent().requestFocus();
        });

        barraPesquisa.setOnAction(pesquisarPorButton.getOnAction());

        HBox boxPesquisarBox = new HBox(10, barraPesquisa, pesquisarPorButton);
        boxPesquisarBox.setAlignment(Pos.CENTER);

        /*** BOTÃO DE CADASTRAR UM NOVO PRODUTO ***/
        Button cadastrarButton = new Button("Cadastrar Produto");
        cadastrarButton.setPrefHeight(30);
        cadastrarButton.setStyle(
                "-fx-background-color: #28a745;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 0 14 0 14;" +
                        "-fx-font-size: 16px;");

        cadastrarButton.setOnMouseEntered(e -> {
            cadastrarButton.setStyle(
                    "-fx-background-color: #4bc56b;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 0 14 0 14;" +
                            "-fx-font-size: 16px;");
            cadastrarButton.setCursor(Cursor.HAND);
        });

        cadastrarButton.setOnMouseExited(e -> {
            cadastrarButton.setStyle(
                    "-fx-background-color: #28a745;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 0 14 0 14;" +
                            "-fx-font-size: 16px;");
            cadastrarButton.setCursor(Cursor.DEFAULT);
        });

        HBox boxButtonBox = new HBox(cadastrarButton);
        boxButtonBox.setAlignment(Pos.CENTER);

        /*** CONTAINER INFERIOR QUE ABRIGA QTD + PESQUISA + CADASTRAR ***/
        BorderPane opcoesInferior = new BorderPane();
        opcoesInferior.setLeft(boxQtdEstoque);
        opcoesInferior.setCenter(boxPesquisarBox);
        opcoesInferior.setRight(boxButtonBox);
        opcoesInferior.setPadding(new Insets(10, 0, 10, 0));

        // TABELA DE PRODUTOS
        this.tabelaProdutos = new TableView<>();

        // Colunas
        TableColumn<Produto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colId.setStyle("-fx-alignment: CENTER;");
        colId.setResizable(false);

        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, String> colCodigoProduto = new TableColumn<>("Código de Barras");
        colCodigoProduto.setCellValueFactory(new PropertyValueFactory<>("codigoDeBarras"));
        colCodigoProduto.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, Double> colPreco = new TableColumn<>("Preço (R$)");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colPreco.setCellFactory(column -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(preco));
                }
            }
        });
        colPreco.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, Double> colDesconto = new TableColumn<>("Desconto");
        colDesconto.setCellValueFactory(new PropertyValueFactory<>("desconto"));
        colDesconto.setCellFactory(column -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double desconto, boolean empty) {
                super.updateItem(desconto, empty);
                if (empty || desconto == null) {
                    setText(null);
                } else {
                    if (desconto >= 0.001 && desconto <= 1.0) {
                        setText(String.format(Locale.of("pt", "BR"), "%.2f%%", desconto * 100));
                    } else {
                        setText("0,00%");
                    }
                }
            }
        });
        colDesconto.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, LocalDateTime> colValidade = new TableColumn<>("Validade");
        colValidade.setCellValueFactory(new PropertyValueFactory<>("validade"));
        colValidade.setCellFactory(column -> new TableCell<Produto, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime validade, boolean empty) {
                super.updateItem(validade, empty);
                if (empty || validade == null) {
                    setText(null);
                } else {
                    setText(validade.format(dateFormatter));
                }
            }
        });
        colValidade.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, Double> colQtdEstoqueOuPeso = new TableColumn<>("Estoque");
        colQtdEstoqueOuPeso.setCellValueFactory(new PropertyValueFactory<>("quantidadeOuPesoEmEstoque"));

        colQtdEstoqueOuPeso.setCellFactory(new Callback<TableColumn<Produto, Double>, TableCell<Produto, Double>>() {

            // Define os formatadores fora do método para melhor performance
            private final NumberFormat decimalFormat = new DecimalFormat("0.00"); // Para KG, L, G
            private final NumberFormat integerFormat = new DecimalFormat("0"); // Para UN, PCT

            @Override
            public TableCell<Produto, Double> call(TableColumn<Produto, Double> param) {
                return new TableCell<Produto, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            Produto produto = getTableView().getItems().get(getIndex());
                            String unidade = produto.getCategoria().getUnidadePadrao();
                            String quantidadeFormatada;
                            if (produto.getCategoria() == Categoria.HORTI) {
                                quantidadeFormatada = decimalFormat.format(item);
                            } else {
                                quantidadeFormatada = integerFormat.format(Math.round(item));
                            }
                            setText(quantidadeFormatada + " " + unidade);
                        }

                        setStyle("-fx-alignment: CENTER;");
                    }
                };
            }
        });
        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setPrefWidth(150);
        colAcoes.setResizable(false);

        EstoqueView self = this;

        colAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox hbox = new HBox(10, btnEditar, btnExcluir);
            {
                btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                btnEditar.setOnMouseEntered(e -> {
                    btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                    btnEditar.setCursor(Cursor.HAND);
                });
                btnEditar.setOnMouseExited(e -> {
                    btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                    btnEditar.setCursor(Cursor.DEFAULT);
                });
                btnEditar.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    EditarProdutoDialog.show(mainApplication.getPrimaryStage(), produto, self::atualizarTabelaCompleta);
                });

                btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                btnExcluir.setOnMouseEntered(e -> {
                    btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                    btnExcluir.setCursor(Cursor.HAND);
                });
                btnExcluir.setOnMouseExited(e -> {
                    btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                    btnExcluir.setCursor(Cursor.DEFAULT);
                });
                btnExcluir.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    ConfirmacaoDialog.show(
                            mainApplication.getPrimaryStage(),
                            "Tem certeza que deseja excluir este produto?",
                            produto.getNome(),
                            "Esta ação é irreversível!",
                            (itemExcluidoNome) -> {
                                if (self.produtoController.deletar(produto.getIdProduto())) {
                                    self.tabelaProdutos.getItems().remove(produto);
                                    self.bancoQtdEstoque
                                            .setText(String.valueOf(self.estoqueController.contarProdutosNoEstoque(1)));
                                    AlertUtils.showSuccess("Sucesso", "Produto Excluído",
                                            "Produto" + itemExcluidoNome + " excluído com sucesso!");
                                } else {
                                    AlertUtils.showError("Erro", "Falha ao Excluir",
                                            "Não foi possível excluir o produto '" + itemExcluidoNome
                                                    + "'. Tente novamente.");
                                }
                            });
                });
                hbox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        this.tabelaProdutos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tabelaProdutos.getColumns().addAll(colId, colNome, colCodigoProduto, colPreco, colDesconto, colValidade,
                colQtdEstoqueOuPeso, colCategoria, colAcoes);

        this.atualizarTabelaCompleta();

        cadastrarButton.setOnAction(e -> {
            CadastroProdutosDialog.show(mainApplication.getPrimaryStage(), this::atualizarTabelaCompleta);
        });

        /*** LAYOUT PRINCIPAL VERTICAL ***/
        VBox vboxConteudo = new VBox(10);
        vboxConteudo.setPadding(new Insets(20, 30, 20, 30));
        vboxConteudo.getChildren().addAll(ScreenTitle.crateHeadBorderPane("Gestão de Estoque"), opcoesInferior,
                this.tabelaProdutos);

        VBox.setVgrow(this.tabelaProdutos, Priority.ALWAYS);

        pane.getChildren().add(vboxConteudo);

        return pane;
    }

    private void atualizarTabelaCompleta() {
        List<Produto> produtosAtualizados = this.produtoController.listarTodos();
        this.listaProdutosNaTabela = FXCollections.observableArrayList(produtosAtualizados);
        this.tabelaProdutos.setItems(this.listaProdutosNaTabela);
        this.bancoQtdEstoque.setText(String.valueOf(this.estoqueController.contarProdutosNoEstoque(1)));
    }
}