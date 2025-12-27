// package br.com.mercadinhoprovidence.view.panel;

// //import br.com.mercadinhoprovidence.controller.ProdutoController;
// import br.com.mercadinhoprovidence.model.Produto;
// //import br.com.mercadinhoprovidence.printer.Impressora;
// import javafx.beans.property.SimpleStringProperty;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.control.*;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.Priority;
// import javafx.scene.layout.VBox;

// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;
// import java.util.List;
// import java.util.stream.Collectors;

// public class PainelEstoqueFX {

//     // ALL members must be static to be accessed from a static method
//    // private static final ProdutoController produtoController = new ProdutoController();
//     private static final TableView<Produto> tabelaProdutos = new TableView<>();
//     private static final ComboBox<String> cbStatusEstoque = new ComboBox<>();
//     private static final ComboBox<String> cbStatusValidade = new ComboBox<>();

//     public static VBox getView() {
//         VBox estoquePane = new VBox(10);
//         estoquePane.setPadding(new Insets(20));
//         estoquePane.setStyle("-fx-background-color: white;");

//         HBox filterBox = new HBox(15);
//         filterBox.setAlignment(Pos.CENTER_LEFT);
//         filterBox.setPadding(new Insets(10));
//         filterBox.setStyle(
//                 "-fx-background-color: #F5F5F5; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

//         Label lblStatusEstoque = new Label("Status Estoque:");
//         cbStatusEstoque.getItems().addAll("Todos", "Alto", "Médio", "Baixo", "Em Falta");
//         cbStatusEstoque.setValue("Todos");
//         cbStatusEstoque.setStyle("-fx-cursor: hand;");

//         Label lblStatusValidade = new Label("Situação Validade:");
//         cbStatusValidade.getItems().addAll("Todos", "Dentro do Prazo", "Vencimento em Breve", "Vencido",
//                 "Sem Validade");
//         cbStatusValidade.setValue("Todos");
//         cbStatusValidade.setStyle("-fx-cursor: hand;");

//         Button btnLimparFiltros = new Button("Limpar Filtros");
//         btnLimparFiltros.setStyle(
//                 "-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");

//         // ✨ ADIÇÃO DO BOTÃO DE IMPRIMIR ✨
//         Button btnImprimir = new Button("Imprimir");
//         btnImprimir.setStyle(
//                 "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");

//         // Adiciona todos os controles à HBox de filtros, incluindo o novo botão
//         filterBox.getChildren().addAll(lblStatusEstoque, cbStatusEstoque, lblStatusValidade, cbStatusValidade);

//         // Crie uma HBox separada para os botões para alinhamento à direita
//         HBox buttonBox = new HBox(10, btnLimparFiltros, btnImprimir);
//         buttonBox.setAlignment(Pos.CENTER_RIGHT);
//         HBox.setHgrow(buttonBox, Priority.ALWAYS); // Permite que a HBox dos botões cresça
//         filterBox.getChildren().add(buttonBox); // Adiciona a HBox de botões ao filterBox

//         setupTable();

//         cbStatusEstoque.setOnAction(e -> aplicarFiltros());
//         cbStatusValidade.setOnAction(e -> aplicarFiltros());
//         btnLimparFiltros.setOnAction(e -> {
//             cbStatusEstoque.setValue("Todos");
//             cbStatusValidade.setValue("Todos");
//             aplicarFiltros();
//         });

//         btnImprimir.setOnAction(e -> {
//             // Pega a lista de produtos atualmente exibida na tabela
//             List<Produto> produtosParaImpressao = tabelaProdutos.getItems();
//             // Chama o método de impressão da classe Impressora
//        //     Impressora.imprimirRelatorioEstoque(produtosParaImpressao);
//         });

//         carregarDadosTabela();

//         VBox.setVgrow(tabelaProdutos, Priority.ALWAYS);
//         estoquePane.getChildren().addAll(filterBox, tabelaProdutos);

//         return estoquePane;
//     }

//     // ... (restante do código da classe PainelEstoqueFX)

//     private static void setupTable() {
//         tabelaProdutos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

//         TableColumn<Produto, String> colCodigoBarras = new TableColumn<>("Código de Barras");
//         colCodigoBarras.setStyle("-fx-alignment: CENTER;");
//         colCodigoBarras.setCellValueFactory(new PropertyValueFactory<>("codigoDeBarras"));

//         TableColumn<Produto, String> colNome = new TableColumn<>("Nome do Produto");
//         colNome.setStyle("-fx-alignment: CENTER;");
//         colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

//         TableColumn<Produto, String> colStatusEstoque = new TableColumn<>("Status do Estoque");
//         colStatusEstoque.setStyle("-fx-alignment: CENTER;");
//         colStatusEstoque.setCellValueFactory(cellData -> {
//             double quantidade = cellData.getValue().getQuantidadeOuPesoEmEstoque();
//             String status = determinarStatusEstoque(quantidade);
//             return new SimpleStringProperty(status);
//         });

//         TableColumn<Produto, String> colSituacaoValidade = new TableColumn<>("Situação (Validade)");
//         colSituacaoValidade.setStyle("-fx-alignment: CENTER;");
//      //   colSituacaoValidade.setCellValueFactory(cellData -> {
// //            LocalDate validade = (cellData.getValue().getValidade() != null)
// //                    ? cellData.getValue().getValidade().toLocalDate()
// //                    : null;
// //            String situacao = determinarSituacaoValidade(validade);
// //            return new SimpleStringProperty(situacao);
//         //});

//         colStatusEstoque.setCellFactory(column -> new TableCell<Produto, String>() {
//             @Override
//             protected void updateItem(String item, boolean empty) {
//                 super.updateItem(item, empty);
//                 getStyleClass().removeAll("alto", "medio", "baixo", "em-falta");
//                 if (empty || item == null) {
//                     setText(null);
//                 } else {
//                     setText(item);
//                     switch (item) {
//                         case "Alto":
//                             getStyleClass().add("alto");
//                             break;
//                         case "Médio":
//                             getStyleClass().add("medio");
//                             break;
//                         case "Baixo":
//                             getStyleClass().add("baixo");
//                             break;
//                         case "Em Falta":
//                             getStyleClass().add("em-falta");
//                             break;
//                     }
//                 }
//             }
//         });

//         tabelaProdutos.getColumns().addAll(colCodigoBarras, colNome, colStatusEstoque, colSituacaoValidade);
//     }

//     private static void carregarDadosTabela() {
//         //List<Produto> todosProdutos = produtoController.listarTodos();
//         //tabelaProdutos.getItems().setAll(todosProdutos);
//     }

//     private static void aplicarFiltros() {
// //        List<Produto> todosProdutos = produtoController.listarTodos();
// //        String filtroEstoque = cbStatusEstoque.getValue();
// //        String filtroValidade = cbStatusValidade.getValue();
// //
// //        List<Produto> produtosFiltrados = todosProdutos.stream()
// //                .filter(p -> {
// //                    String statusEstoque = determinarStatusEstoque(p.getQuantidadeOuPesoEmEstoque());
// //                    String statusValidade = determinarSituacaoValidade(
// //                            (p.getValidade() != null) ? p.getValidade().toLocalDate() : null);
// //
// //                    boolean passaNoFiltroEstoque = "Todos".equals(filtroEstoque) || statusEstoque.equals(filtroEstoque);
// //                    boolean passaNoFiltroValidade = "Todos".equals(filtroValidade)
// //                            || statusValidade.equals(filtroValidade);
// //
// //                    return passaNoFiltroEstoque && passaNoFiltroValidade;
// //                })
// //                .collect(Collectors.toList());
// //
// //        tabelaProdutos.getItems().setAll(produtosFiltrados);
//     }

//     private static String determinarStatusEstoque(Double quantidade) {
//         if (quantidade <= 0)
//             return "Em Falta";
//         if (quantidade < 10)
//             return "Baixo";
//         if (quantidade < 50)
//             return "Médio";
//         return "Alto";
//     }

//     private static String determinarSituacaoValidade(LocalDate validade) {
//         if (validade == null)
//             return "Sem Validade";
//         LocalDate hoje = LocalDate.now();
//         if (validade.isBefore(hoje)) {
//             return "Vencido";
//         }
//         long diasParaVencer = ChronoUnit.DAYS.between(hoje, validade);
//         if (diasParaVencer <= 30) {
//             return "Vencimento em Breve";
//         }
//         return "Dentro do Prazo";
//     }
// }