package br.com.mercadinhoprovidence.view;

import java.math.BigDecimal;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.controller.EstoqueController;
import br.com.mercadinhoprovidence.controller.ProductController;
import br.com.mercadinhoprovidence.model.Employee;
import br.com.mercadinhoprovidence.model.enums.Category;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.view.component.ScreenTitle; // criado para sanar erros (precisa de verificação)
import br.com.mercadinhoprovidence.view.dialogs.CadastroProdutosDialog; // criado para sanar erros (precisa de verificação)
import br.com.mercadinhoprovidence.view.dialogs.ConfirmarDialog;
import br.com.mercadinhoprovidence.view.dialogs.EditarProdutoDialog; // criado para sanar erros (precisa de verificação)
import br.com.mercadinhoprovidence.dto.produto.ProductTableDto;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EstoqueView {

    final private MainApplication mainApplication;
    @SuppressWarnings("unused")
    final private Employee funcionarioLogado;
    final private StackPane viewPane;
    final private ProductController produtoController;
    final private EstoqueController estoqueController;

    private ObservableList<ProductTableDto> listaProdutosNaTabela;
    private TableView<ProductTableDto> tabelaProdutos;
    private Label bancoQtdEstoque;

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public EstoqueView(MainApplication mainApplication, Employee funcionarioLogado, ProductController produtoController,
            EstoqueController estoqueController) {
        this.mainApplication = mainApplication;
        this.funcionarioLogado = funcionarioLogado;
        this.produtoController = produtoController;
        this.estoqueController = estoqueController;
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

        this.bancoQtdEstoque = new Label(String.valueOf(estoqueController.produtosCadastradosNoEstoque(1)));
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

            ProductTableDto produtoEncontrado = this.produtoController.findByCode(codigoInseridoPesquisa);

            if (produtoEncontrado != null) {
                final ProductTableDto finalProduto = produtoEncontrado;
                boolean foundInTable = false;

                for (ProductTableDto p : this.listaProdutosNaTabela) {
                    if (p.getIdProduto().equals(finalProduto.getIdProduto())) {
                        tabelaProdutos.getSelectionModel().select(p);
                        tabelaProdutos.scrollTo(p);
                        foundInTable = true;

                        System.out.println(
                                "Produto encontrado e selecionado na tabela: " + p.getNome());

                        break;
                    }
                }

                if (!foundInTable) {
                    System.out.println(
                            "Produto encontrado no BD, mas não visível na tabela. Recarregando e tentando novamente.");
                    atualizarTabelaCompleta();
                    for (ProductTableDto p : this.listaProdutosNaTabela) {
                        if (p.getIdProduto().equals(finalProduto.getIdProduto())) {
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
        TableColumn<ProductTableDto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colId.setStyle("-fx-alignment: CENTER;");
        colId.setResizable(false);

        TableColumn<ProductTableDto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setStyle("-fx-alignment: CENTER;");

        TableColumn<ProductTableDto, String> colCodigoProduto = new TableColumn<>("Código de Barras");
        colCodigoProduto.setCellValueFactory(new PropertyValueFactory<>("codigoDeBarras"));
        colCodigoProduto.setStyle("-fx-alignment: CENTER;");

        TableColumn<ProductTableDto, BigDecimal> colPreco = new TableColumn<>("Preço (R$)");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colPreco.setCellFactory(column -> new TableCell<ProductTableDto, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(preco));
                }
            }
        });
        colPreco.setStyle("-fx-alignment: CENTER;");

        TableColumn<ProductTableDto, BigDecimal> colDesconto = new TableColumn<>("Desconto");
        colDesconto.setCellValueFactory(new PropertyValueFactory<>("desconto"));
        colDesconto.setCellFactory(column -> new TableCell<ProductTableDto, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal desconto, boolean empty) {
                super.updateItem(desconto, empty);
                if (empty || desconto == null) {
                    setText(null);
                } else {
                        if (desconto.compareTo(BigDecimal.valueOf(0.001)) >= 0
                            && desconto.compareTo(BigDecimal.ONE) <= 0) {
                        setText(String.format(Locale.of("pt", "BR"), "%.2f%%",
                            desconto.multiply(BigDecimal.valueOf(100))));
                    } else {
                        setText("0,00%");
                    }
                }
            }
        });
        colDesconto.setStyle("-fx-alignment: CENTER;");

        TableColumn<ProductTableDto, Date> colValidade = new TableColumn<>("Validade");
        colValidade.setCellValueFactory(new PropertyValueFactory<>("validade"));
        colValidade.setCellFactory(column -> new TableCell<ProductTableDto, Date>() {
            @Override
            protected void updateItem(Date validade, boolean empty) {
                super.updateItem(validade, empty);
                if (empty || validade == null) {
                    setText(null);
                } else {
                    setText(new SimpleDateFormat("dd/MM/yyyy").format(validade));
                }
            }
        });
        colValidade.setStyle("-fx-alignment: CENTER;");

        TableColumn<ProductTableDto, BigDecimal> colQtdEstoqueOuPeso = new TableColumn<>("Estoque");
        colQtdEstoqueOuPeso.setCellValueFactory(new PropertyValueFactory<>("quantidadeOuPesoEmEstoque"));

        colQtdEstoqueOuPeso.setCellFactory(new Callback<TableColumn<ProductTableDto, BigDecimal>, TableCell<ProductTableDto, BigDecimal>>() {

            // Define os formatadores fora do método para melhor performance
            private final NumberFormat decimalFormat = new DecimalFormat("0.00"); // Para KG, L, G
            private final NumberFormat integerFormat = new DecimalFormat("0"); // Para UN, PCT

            @Override
            public TableCell<ProductTableDto, BigDecimal> call(TableColumn<ProductTableDto, BigDecimal> param) {
                return new TableCell<ProductTableDto, BigDecimal>() {
                    @Override
                    protected void updateItem(BigDecimal item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            ProductTableDto produto = getTableView().getItems().get(getIndex());
                            String unidade = produto.getCategoria().getUnit();
                            String quantidadeFormatada;
                            if (produto.getCategoria() == Category.HORTI) {
                                quantidadeFormatada = decimalFormat.format(item);
                            } else {
                                quantidadeFormatada = integerFormat.format(item.setScale(0, java.math.RoundingMode.HALF_UP));
                            }
                            setText(quantidadeFormatada + " " + unidade);
                        }

                        setStyle("-fx-alignment: CENTER;");
                    }
                };
            }
        });
        TableColumn<ProductTableDto, Category> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setStyle("-fx-alignment: CENTER;");

        TableColumn<ProductTableDto, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setPrefWidth(150);
        colAcoes.setResizable(false);

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
                    ProductTableDto produto = getTableView().getItems().get(getIndex());
                    EditarProdutoDialog.show(mainApplication.getPrimaryStage(), produto, EstoqueView.this::atualizarTabelaCompleta);
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
                    ProductTableDto produto = getTableView().getItems().get(getIndex());
                    ConfirmarDialog.show(
                            mainApplication.getPrimaryStage(),
                            "Tem certeza que deseja excluir este produto?",
                            produto.getNome(),
                            "Esta ação é irreversível!",
                            (itemExcluidoNome) -> {
                                if (EstoqueView.this.produtoController.delete(produto.getIdProduto())) {
                                    EstoqueView.this.tabelaProdutos.getItems().remove(produto);
                                    EstoqueView.this.bancoQtdEstoque
                                            .setText(String
                                                    .valueOf(EstoqueView.this.estoqueController.produtosCadastradosNoEstoque(1)));
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
        this.tabelaProdutos.getColumns().addAll(colId, colNome, colCodigoProduto, colPreco, colDesconto, colValidade, colQtdEstoqueOuPeso, colCategoria, colAcoes);

        this.atualizarTabelaCompleta();

        cadastrarButton.setOnAction(e -> {
            CadastroProdutosDialog.show(mainApplication.getPrimaryStage(), this::atualizarTabelaCompleta);
        });

        /*** LAYOUT PRINCIPAL VERTICAL ***/
        VBox vboxConteudo = new VBox(10);
        vboxConteudo.setPadding(new Insets(20, 30, 20, 30));
        vboxConteudo.getChildren().addAll(ScreenTitle.createHeadBorderPane("Gestão de Estoque"), opcoesInferior,
                this.tabelaProdutos);

        VBox.setVgrow(this.tabelaProdutos, Priority.ALWAYS);

        pane.getChildren().add(vboxConteudo);

        return pane;
    }

    private void atualizarTabelaCompleta() {
        List<ProductTableDto> produtosAtualizados = this.produtoController.findAll();
        this.listaProdutosNaTabela = FXCollections.observableArrayList(produtosAtualizados);
        this.tabelaProdutos.setItems(this.listaProdutosNaTabela);
        this.bancoQtdEstoque.setText(String.valueOf(this.estoqueController.produtosCadastradosNoEstoque(1)));
    }
}