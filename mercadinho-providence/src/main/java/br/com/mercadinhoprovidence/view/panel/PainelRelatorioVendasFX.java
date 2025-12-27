package br.com.mercadinhoprovidence.view.panel;

//import br.com.mercadinhoprovidence.dao.FuncionarioDao;
//import br.com.mercadinhoprovidence.dao.VendaDAO;
//import br.com.mercadinhoprovidence.model.ItemVenda;
//import br.com.mercadinhoprovidence.model.Venda;
//import br.com.mercadinhoprovidence.model.relatorio.ProdutosMaisVendido;
//import br.com.mercadinhoprovidence.model.relatorio.VendasPorPagamento;
//import br.com.mercadinhoprovidence.printer.Impressora;
//import javafx.beans.property.SimpleDoubleProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//
//import java.math.BigDecimal;
//import java.text.NumberFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;

import javafx.scene.layout.VBox;

public class PainelRelatorioVendasFX {

    private enum TipoRelatorio {
        DIARIO, FUNCIONARIO, VENDIDOS, PAGAMENTO
    }

    private static TipoRelatorio tipoAtual = TipoRelatorio.DIARIO;

    public static VBox getView() {
//        VBox salesPane = new VBox(10);
//        salesPane.setPadding(new Insets(20));
//        salesPane.setStyle("-fx-background-color: white;");
//
//        // --- Painel de Filtros e Botões (Sem alterações) ---
//        HBox filterBox = new HBox(15);
//        filterBox.setAlignment(Pos.CENTER_LEFT);
//        filterBox.setPadding(new Insets(10));
//        filterBox.setStyle(
//                "-fx-background-color: #F5F5F5; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
//
//        filterBox.getChildren().add(new Label("Período:"));
//        filterBox.getChildren().add(new Label("De:"));
//        DatePicker startDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
//        filterBox.getChildren().add(startDatePicker);
//        filterBox.getChildren().add(new Label("Até:"));
//        DatePicker endDatePicker = new DatePicker(LocalDate.now());
//        filterBox.getChildren().add(endDatePicker);
//
//        Button btnGerar = new Button("Gerar Relatório");
//        btnGerar.setStyle(
//                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
//        filterBox.getChildren().add(btnGerar);
//
//        Button btnExportar = new Button("Imprimir");
//        btnExportar.setStyle(
//                "-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
//
//        filterBox.getChildren().add(btnExportar);
//
//        salesPane.getChildren().add(filterBox);
//
//        // --- Botões de Tipos de Relatório de Vendas (Sem alterações) ---
//        HBox reportTypeButtons = new HBox(10);
//        reportTypeButtons.setPadding(new Insets(10, 0, 0, 0));
//        reportTypeButtons.setAlignment(Pos.CENTER_LEFT);
//
//        Button btnVendasDiarias = new Button("Vendas Diárias Detalhadas");
//        Button btnVendasFormaPagamento = new Button("Vendas por Forma de Pagamento");
//        Button btnProdutosMaisVendidos = new Button("Produtos Mais Vendidos");
//        Button btnVendasFuncionario = new Button("Vendas por Funcionário");
//
//        styleReportTypeButton(btnVendasDiarias);
//        styleReportTypeButton(btnVendasFormaPagamento);
//        styleReportTypeButton(btnProdutosMaisVendidos);
//        styleReportTypeButton(btnVendasFuncionario);
//
//        reportTypeButtons.getChildren().addAll(btnVendasDiarias, btnVendasFormaPagamento, btnProdutosMaisVendidos,
//                btnVendasFuncionario);
//        salesPane.getChildren().add(reportTypeButtons);
//
//        TableView<Venda> tableViewVendas = new TableView<>();
//        TableView<ProdutosMaisVendido> tableViewProdutos = new TableView<>();
//        TableView<VendasPorPagamento> tableViewPagamento = new TableView<>();
//
//        tableViewPagamento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tableViewProdutos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        tableViewVendas.setVisible(true);
//        tableViewProdutos.setVisible(false);
//        tableViewPagamento.setVisible(false);
//
//        tableViewProdutos.managedProperty().bind(tableViewProdutos.visibleProperty());
//        tableViewVendas.managedProperty().bind(tableViewVendas.visibleProperty());
//        tableViewPagamento.managedProperty().bind(tableViewPagamento.visibleProperty());
//
//        VBox.setVgrow(tableViewVendas, Priority.ALWAYS);
//        VBox.setVgrow(tableViewProdutos, Priority.ALWAYS);
//        VBox.setVgrow(tableViewPagamento, Priority.ALWAYS);
//
//        salesPane.getChildren().addAll(tableViewVendas, tableViewProdutos, tableViewPagamento);
//
//        Label placeholderLabel = new Label("Nenhum relatório para o período selecionado.");
//        placeholderLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 16px;");
//
//        // ALTERADO: AS COLUNAS ESTÃO AGORA LIGADAS À CLASSE VENDA
//        TableColumn<Venda, LocalDate> dataCol = new TableColumn<>("Data");
//        dataCol.setCellValueFactory(cellData -> {
//            LocalDate localDate = cellData.getValue().getDataHora().toLocalDate();
//            return new javafx.beans.property.SimpleObjectProperty<>(localDate);
//        });
//        dataCol.setStyle("-fx-alignment: CENTER;");
//        dataCol.setCellFactory(column -> new TableCell<>() {
//            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//            @Override
//            protected void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(formatter.format(item));
//                }
//            }
//        });
//
//        TableColumn<Venda, String> operadorCol = new TableColumn<>("Operador");
//        operadorCol.setCellValueFactory(cellData -> {
//            FuncionarioDao funcionarioDao = new FuncionarioDao();
//            Map<Integer, String> funcionarios = funcionarioDao.buscarTodosComoMap();
//            String nomeOperador = funcionarios.getOrDefault(cellData.getValue().getIdFuncionario(), "Desconhecido");
//            return new SimpleStringProperty(nomeOperador);
//        });
//        operadorCol.setCellFactory(column -> new TableCell<Venda, String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(item);
//                    // Aplica o alinhamento ao conteúdo da célula
//                    setAlignment(Pos.CENTER);
//                }
//            }
//        });
//
//        TableColumn<Venda, Double> qtdeItensCol = new TableColumn<>("Qtde Itens");
//        qtdeItensCol.setCellValueFactory(cellData -> {
//            double totalItens = cellData.getValue().getItensVenda().stream()
//                    .mapToDouble(ItemVenda::getQuantidadeOuPeso)
//                    .sum();
//            return new SimpleDoubleProperty(totalItens).asObject();
//        });
//        qtdeItensCol.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<Venda, Double> subtotalCol = new TableColumn<>("Subtotal");
//        subtotalCol.setCellValueFactory(new PropertyValueFactory<>("valorSubtotal"));
//        subtotalCol.setStyle("-fx-alignment: CENTER;");
//        subtotalCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(nf.format(item));
//                }
//            }
//        });
//
//        TableColumn<Venda, Double> descontoCol = new TableColumn<>("Desconto");
//        descontoCol.setCellValueFactory(new PropertyValueFactory<>("valorDesconto"));
//        descontoCol.setStyle("-fx-alignment: CENTER;");
//        descontoCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setText(null);
//                } else if (item == null || item.compareTo(0.0) == 0) {
//                    setText(nf.format(0.0));
//                } else {
//                    setText(nf.format(item));
//                }
//            }
//        });
//
//        TableColumn<Venda, Double> totalCol = new TableColumn<>("Total");
//        totalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
//        totalCol.setStyle("-fx-alignment: CENTER;");
//        totalCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(nf.format(item));
//                }
//            }
//        });
//
//        TableColumn<Venda, Void> detalhesCol = new TableColumn<>("Detalhes");
//        detalhesCol.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button("Ver detalhes");
//            {
//                btn.setStyle(
//                        "-fx-background-color: #17A2B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
//                btn.setOnAction(e -> {
//                    Venda venda = getTableView().getItems().get(getIndex());
//                    mostrarDetalhesDaVenda(venda);
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setAlignment(Pos.CENTER);
//                    setGraphic(btn);
//                }
//            }
//        });
//
//        tableViewVendas.getColumns().addAll(dataCol, operadorCol, qtdeItensCol, subtotalCol, descontoCol, totalCol,
//                detalhesCol);
//
//        tableViewVendas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        // --- Lógica do Botão 'Gerar Relatório' ---
//        // --- Lógica do Botão 'Gerar Relatório' ---
//        btnGerar.setOnAction(e -> {
//            LocalDate start = startDatePicker.getValue();
//            LocalDate end = endDatePicker.getValue();
//
//            if (start == null || end == null) {
//                new Alert(Alert.AlertType.WARNING, "Por favor, selecione as datas inicial e final.").showAndWait();
//                return;
//            }
//
//            List<Venda> vendas = buscarVendasPorPeriodo(start, end);
//
//            if (tipoAtual == TipoRelatorio.FUNCIONARIO) {
//                List<Venda> agrupadas = agruparVendasPorFuncionario(vendas);
//                tableViewVendas.getItems().setAll(agrupadas);
//                tableViewVendas.setVisible(true);
//                tableViewProdutos.setVisible(false);
//                tableViewPagamento.setVisible(false);
//            } else if (tipoAtual == TipoRelatorio.VENDIDOS) {
//                List<ProdutosMaisVendido> produtosAgregados = agruparProdutosMaisVendidos(vendas);
//                tableViewProdutos.getItems().setAll(produtosAgregados);
//                tableViewVendas.setVisible(false);
//                tableViewProdutos.setVisible(true);
//                tableViewPagamento.setVisible(false);
//            } else if (tipoAtual == TipoRelatorio.PAGAMENTO) { // Lógica limpa
//                List<VendasPorPagamento> agrupadasPorPagamento = agruparVendasPorFormaPagamento(vendas);
//                tableViewPagamento.getItems().setAll(agrupadasPorPagamento);
//                tableViewVendas.setVisible(false);
//                tableViewProdutos.setVisible(false);
//                tableViewPagamento.setVisible(true);
//            } else {
//                tableViewVendas.getItems().setAll(vendas);
//                tableViewVendas.setVisible(true);
//                tableViewProdutos.setVisible(false);
//                tableViewPagamento.setVisible(false);
//            }
//        });
//
//        btnVendasDiarias.setOnAction(e -> {
//            tipoAtual = TipoRelatorio.DIARIO;
//            configurarTabelaVendasDiarias(tableViewVendas); // Reconfigura as colunas
//            tableViewVendas.setVisible(true);
//            tableViewProdutos.setVisible(false);
//            tableViewPagamento.setVisible(false); // Exibe a nova tabela
//            tableViewVendas.getItems().clear();
//        });
//
//        btnVendasFormaPagamento.setOnAction(e -> {
//            tipoAtual = TipoRelatorio.PAGAMENTO;
//            configurarTabelaVendasPorPagamento(tableViewPagamento); // Usa a nova tabela
//            tableViewVendas.setVisible(false);
//            tableViewProdutos.setVisible(false);
//            tableViewPagamento.setVisible(true); // Exibe a nova tabela
//            tableViewPagamento.getItems().clear();
//        });
//
//        btnVendasFuncionario.setOnAction(e -> {
//            tipoAtual = TipoRelatorio.FUNCIONARIO;
//            configurarTabelaVendasPorFuncionario(tableViewVendas);
//            tableViewVendas.setVisible(true);
//            tableViewProdutos.setVisible(false);
//            tableViewPagamento.setVisible(false); // Exibe a nova tabela
//
//            tableViewVendas.getItems().clear(); // Limpa os dados anteriores
//        });
//
//        btnExportar.setOnAction(e -> {
//            if (tipoAtual == TipoRelatorio.FUNCIONARIO) {
//                Impressora.imprimirRelatorioFuncionario(tableViewVendas.getItems());
//            } else if (tipoAtual == TipoRelatorio.DIARIO) {
//                Impressora.imprimirRelatorioVendas(tableViewVendas.getItems());
//            }
//        });
//
//        btnProdutosMaisVendidos.setOnAction(e -> {
//            tipoAtual = TipoRelatorio.VENDIDOS;
//            configurarTabelaProdutosMaisVendidos(tableViewProdutos);
//            tableViewVendas.setVisible(false);
//            tableViewProdutos.setVisible(true);
//            tableViewPagamento.setVisible(false); // Exibe a nova tabela
//            tableViewProdutos.getItems().clear();
//        });
//
//        return salesPane;
//    }
//
//    // Métodos auxiliares
//    private static List<Venda> buscarVendasPorPeriodo(LocalDate inicio, LocalDate fim) {
//        VendaDAO vendaDao = new VendaDAO();
//        return vendaDao.buscarPorPeriodo(inicio, fim);
//    }
//
//    private static void configurarTabelaVendasPorFuncionario(TableView<Venda> tableView) {
//        tableView.getColumns().clear();
//
//        TableColumn<Venda, String> funcionarioCol = new TableColumn<>("Funcionário");
//        funcionarioCol.setCellValueFactory(cellData -> {
//            FuncionarioDao dao = new FuncionarioDao();
//            Map<Integer, String> map = dao.buscarTodosComoMap();
//            return new SimpleStringProperty(map.getOrDefault(cellData.getValue().getIdFuncionario(), "Desconhecido"));
//        });
//        funcionarioCol.setStyle("-fx-alignment: CENTER;");
//        funcionarioCol.setPrefWidth(200);
//
//        TableColumn<Venda, Double> totalCol = new TableColumn<>("Total Vendido");
//        totalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotalManual"));
//        totalCol.setPrefWidth(150);
//        totalCol.setStyle("-fx-alignment: CENTER;");
//
//        totalCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(nf.format(item));
//                }
//            }
//        });
//
//        tableView.getColumns().addAll(funcionarioCol, totalCol);
//    }
//
//    private static void configurarTabelaVendasDiarias(TableView<Venda> tableView) {
//        tableView.getColumns().clear();
//
//        TableColumn<Venda, LocalDate> dataCol = new TableColumn<>("Data");
//        dataCol.setCellValueFactory(cellData -> {
//            LocalDate localDate = cellData.getValue().getDataHora().toLocalDate();
//            return new javafx.beans.property.SimpleObjectProperty<>(localDate);
//        });
//        dataCol.setPrefWidth(120);
//        dataCol.setStyle("-fx-alignment: CENTER;");
//        dataCol.setCellFactory(column -> new TableCell<>() {
//            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//            @Override
//            protected void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : formatter.format(item));
//            }
//        });
//
//        TableColumn<Venda, String> operadorCol = new TableColumn<>("Operador");
//        operadorCol.setCellValueFactory(cellData -> {
//            FuncionarioDao funcionarioDao = new FuncionarioDao();
//            Map<Integer, String> funcionarios = funcionarioDao.buscarTodosComoMap();
//            String nomeOperador = funcionarios.getOrDefault(cellData.getValue().getIdFuncionario(), "Desconhecido");
//            return new SimpleStringProperty(nomeOperador);
//        });
//        operadorCol.setPrefWidth(150);
//
//        TableColumn<Venda, Double> qtdeItensCol = new TableColumn<>("Qtde Itens");
//        qtdeItensCol.setCellValueFactory(cellData -> {
//            double totalItensDouble = cellData.getValue().getItensVenda().stream()
//                    .mapToDouble(ItemVenda::getQuantidadeOuPeso)
//                    .sum();
//            return new SimpleDoubleProperty(totalItensDouble).asObject();
//        });
//        qtdeItensCol.setPrefWidth(100);
//        qtdeItensCol.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<Venda, Double> subtotalCol = new TableColumn<>("Subtotal");
//        subtotalCol.setCellValueFactory(new PropertyValueFactory<>("valorSubtotal"));
//        subtotalCol.setPrefWidth(120);
//        subtotalCol.setStyle("-fx-alignment: CENTER;");
//        subtotalCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : nf.format(item));
//            }
//        });
//
//        TableColumn<Venda, Double> descontoCol = new TableColumn<>("Desconto");
//        descontoCol.setCellValueFactory(new PropertyValueFactory<>("valorDesconto"));
//        descontoCol.setPrefWidth(120);
//        descontoCol.setStyle("-fx-alignment: CENTER;");
//        descontoCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setText(null);
//                } else if (item == null || item.compareTo(0.0) == 0) {
//                    setText(nf.format(0.0));
//                } else {
//                    setText(nf.format(item));
//                }
//            }
//        });
//
//        TableColumn<Venda, Double> totalCol = new TableColumn<>("Total");
//        totalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
//        totalCol.setPrefWidth(120);
//        totalCol.setStyle("-fx-alignment: CENTER;");
//        totalCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : nf.format(item));
//            }
//        });
//
//        TableColumn<Venda, Void> detalhesCol = new TableColumn<>("Detalhes");
//        detalhesCol.setPrefWidth(140);
//        detalhesCol.setStyle("-fx-alignment: CENTER;");
//        detalhesCol.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button("Ver detalhes");
//            {
//                btn.setStyle("-fx-background-color: #17A2B8; -fx-text-fill: white; -fx-font-weight: bold;");
//                btn.setOnAction(e -> {
//                    Venda venda = getTableView().getItems().get(getIndex());
//                    mostrarDetalhesDaVenda(venda);
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty ? null : btn);
//            }
//        });
//
//        tableView.getColumns().addAll(dataCol, operadorCol, qtdeItensCol, subtotalCol, descontoCol, totalCol,
//                detalhesCol);
//
//    }
//
//    // Dentro da classe PainelRelatorioVendasFX
//
//    // O tipo da TableView agora é genérico para acomodar a mudança
//    private static void configurarTabelaProdutosMaisVendidos(TableView tableView) {
//        tableView.getColumns().clear();
//
//        // Coluna para o nome do produto
//        TableColumn<ProdutosMaisVendido, String> produtoCol = new TableColumn<>("Produtos");
//        produtoCol.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
//        produtoCol.setStyle("-fx-alignment: CENTER;");
//        produtoCol.setPrefWidth(300);
//
//        // Coluna para a quantidade total vendida
//        TableColumn<ProdutosMaisVendido, Integer> qtdeVendidaCol = new TableColumn<>("Qtd Total");
//        qtdeVendidaCol.setCellValueFactory(new PropertyValueFactory<>("quantidadeVendida"));
//        qtdeVendidaCol.setPrefWidth(150);
//        qtdeVendidaCol.setStyle("-fx-alignment: CENTER;");
//
//        // Coluna para o faturamento total
//        TableColumn<ProdutosMaisVendido, BigDecimal> faturamentoCol = new TableColumn<>("Total de Vendas");
//        faturamentoCol.setCellValueFactory(new PropertyValueFactory<>("faturamentoTotal"));
//        faturamentoCol.setPrefWidth(150);
//        faturamentoCol.setStyle("-fx-alignment: CENTER;");
//        faturamentoCol.setCellFactory(column -> new TableCell<ProdutosMaisVendido, BigDecimal>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(BigDecimal item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : nf.format(item));
//            }
//        });
//
//        tableView.getColumns().addAll(produtoCol, qtdeVendidaCol, faturamentoCol);
//    }
//
//    private static void configurarTabelaVendasPorPagamento(TableView<VendasPorPagamento> tableView) {
//        tableView.getColumns().clear(); // Limpa as colunas antigas
//
//        TableColumn<VendasPorPagamento, String> formaPagamentoCol = new TableColumn<>("Forma de Pagamento");
//        formaPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("tipoPagamento"));
//        formaPagamentoCol.setPrefWidth(200);
//        formaPagamentoCol.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<VendasPorPagamento, Double> totalCol = new TableColumn<>("Total Arrecadado");
//        totalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
//        totalCol.setPrefWidth(150);
//        totalCol.setStyle("-fx-alignment: CENTER;");
//
//        totalCol.setCellFactory(column -> new TableCell<>() {
//            private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
//
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : nf.format(item));
//            }
//        });
//
//        tableView.getColumns().addAll(formaPagamentoCol, totalCol);
//    }
//
//    // NOVO MÉTODO PARA EXIBIR DETALHES DE UMA VENDA INDIVIDUAL
//    public static void mostrarDetalhesDaVenda(Venda venda) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Detalhes da Venda");
//        alert.setHeaderText("Venda realizada em "
//                + venda.getDataHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//
//        StringBuilder content = new StringBuilder();
//        content.append("ID da Venda: ").append(venda.getIdVenda()).append("\n");
//        content.append("Operador: ").append(
//                new FuncionarioDao().buscarTodosComoMap().getOrDefault(venda.getIdFuncionario(), "Desconhecido"))
//                .append("\n");
//        content.append("Subtotal: R$ ").append(String.format("%.2f", venda.getValorSubtotal())).append("\n");
//        content.append("Desconto: R$ ").append(String.format("%.2f", venda.getValorDesconto())).append("\n");
//        content.append("Total: R$ ").append(String.format("%.2f", venda.getValorTotal())).append("\n\n");
//        content.append("--- ITENS DA VENDA ---\n");
//
//        for (ItemVenda item : venda.getItensVenda()) {
//            content.append("Produto: ").append(item.getProduto().getNome()).append("\n");
//            content.append("  - Qtde: ").append(item.getQuantidadeOuPeso()).append("\n");
//            content.append("  - Preço Unitário: R$ ").append(String.format("%.2f", item.getPrecoUnitarioVenda()))
//                    .append("\n");
//            content.append("  - Subtotal do Item: R$ ").append(String.format("%.2f", item.getTotalItem()))
//                    .append("\n\n");
//        }
//
//        alert.setContentText(content.toString());
//        alert.showAndWait();
//    }
//
//    private static void styleReportTypeButton(Button button) {
//        button.setStyle(
//                "-fx-background-color: #E0E0E0; -fx-text-fill: #333333; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
//        button.setOnMouseEntered(e -> button.setStyle(
//                "-fx-background-color: #D0D0D0; -fx-text-fill: #333333; -fx-font-weight: bold; -fx-background-radius: 5;  -fx-cursor: hand;"));
//        button.setOnMouseExited(e -> button.setStyle(
//                "-fx-background-color: #E0E0E0; -fx-text-fill: #333333; -fx-font-weight: bold; -fx-background-radius: 5;  -fx-cursor: hand;"));
//    }
//
//    private static List<Venda> agruparVendasPorFuncionario(List<Venda> vendas) {
//        Map<Integer, Double> totalPorFuncionario = vendas.stream()
//                .collect(Collectors.groupingBy(
//                        Venda::getIdFuncionario,
//                        Collectors.summingDouble(Venda::getValorTotal)));
//
//        return totalPorFuncionario.entrySet().stream()
//                .map(entry -> {
//                    Venda v = new Venda();
//                    v.setIdFuncionario(entry.getKey());
//                    v.setValorTotalManual(entry.getValue());
//                    return v;
//                }).collect(Collectors.toList());
//    }
//
//    private static List<ProdutosMaisVendido> agruparProdutosMaisVendidos(List<Venda> vendas) {
//        Map<Integer, ProdutosMaisVendido> produtosAgregados = new HashMap<>();
//
//        for (Venda venda : vendas) {
//            for (ItemVenda item : venda.getItensVenda()) {
//                int produtoId = item.getProduto().getIdProduto();
//
//                if (produtosAgregados.containsKey(produtoId)) {
//                    ProdutosMaisVendido produtoExistente = produtosAgregados.get(produtoId);
//                    produtoExistente
//                            .setQuantidadeVendida(produtoExistente.getQuantidadeVendida() + item.getQuantidadeOuPeso()); // aqui
//                    BigDecimal novoFaturamento = produtoExistente.getFaturamentoTotal()
//                            .add(BigDecimal.valueOf(item.getTotalItem()));
//                    produtoExistente.setFaturamentoTotal(novoFaturamento);
//                } else {
//                    ProdutosMaisVendido novoProduto = new ProdutosMaisVendido();
//                    novoProduto.setNomeProduto(item.getProduto().getNome());
//                    novoProduto.setCodigoDeBarras(item.getProduto().getCodigoDeBarras());
//                    novoProduto.setQuantidadeVendida(item.getQuantidadeOuPeso()); // Erro aqui
//                    novoProduto.setFaturamentoTotal(BigDecimal.valueOf(item.getTotalItem()));
//                    produtosAgregados.put(produtoId, novoProduto);
//                }
//            }
//        }
//
//        // Converte o mapa para uma lista e ordena por quantidade vendida
//        List<ProdutosMaisVendido> listaFinal = produtosAgregados.values().stream()
//                .sorted(Comparator.comparing(ProdutosMaisVendido::getQuantidadeVendida).reversed())
//                .collect(Collectors.toList());
//
//        return listaFinal;
//    }
//
//    private static List<VendasPorPagamento> agruparVendasPorFormaPagamento(List<Venda> vendas) {
//        Map<String, Double> totaisPorPagamento = new HashMap<>();
//
//        for (Venda venda : vendas) {
//            if (venda.getPagamento() != null && venda.getPagamento().getForma() != null) {
//                String tipo = venda.getPagamento().getForma().toString();
//                double valor = venda.getPagamento().getValorPago();
//                totaisPorPagamento.put(tipo, totaisPorPagamento.getOrDefault(tipo, 0.0) + valor);
//            }
//        }
//
//        List<VendasPorPagamento> resultado = new ArrayList<>();
//        for (Map.Entry<String, Double> entry : totaisPorPagamento.entrySet()) {
//            String tipoFormatado = formatarTipoPagamento(entry.getKey());
//            resultado.add(new VendasPorPagamento(tipoFormatado, entry.getValue()));
//        }
//        return resultado;
//    }
//
//    // Método auxiliar para formatar a string
//    private static String formatarTipoPagamento(String tipo) {
//        if ("CARTAO_DEBITO".equals(tipo)) {
//            return "Cartão Débito";
//        } else if ("CARTAO_CREDITO".equals(tipo)) {
//            return "Cartão Crédito";
//        } else if ("DINHEIRO".equals(tipo)) {
//            return "Dinheiro";
//        } else if ("PIX".equals(tipo)) {
//            return "PIX";
//        }
//        return tipo;
//    }
        return null;
    }
}