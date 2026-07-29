//package br.com.mercadinhoprovidence.view;
//
//import br.com.mercadinhoprovidence.controller.FuncionarioController;
//import br.com.mercadinhoprovidence.controller.ProdutoController;
//import br.com.mercadinhoprovidence.controller.VendaController;
//import br.com.mercadinhoprovidence.model.Funcionario;
//import br.com.mercadinhoprovidence.model.ItemVenda;
//import br.com.mercadinhoprovidence.model.Produto;
//import br.com.mercadinhoprovidence.model.Venda;
//import br.com.mercadinhoprovidence.model.enums.Cargo;
//import br.com.mercadinhoprovidence.util.AlertUtils;
//import br.com.mercadinhoprovidence.util.TimeUtils;
//import br.com.mercadinhoprovidence.view.dialogs.TelaConfirmacaoVendaPagamentoView;
//import javafx.application.Platform;
//import javafx.geometry.HPos;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.*;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//import java.util.Optional;
//import java.util.function.Consumer;
//
//public class TelaVenda extends VBox {
//
//	private Label quantidadeValorLabel;
//	private Label precoUnitarioLabel;
//	private Label nomeProdutoLabel;
//	private Label unidadePrecoLabel;
//	private Label totalItemAtualLabel;
//	private Label totalCompraLabel;
//	private Label statusModoLabel;
//
//	private boolean modoRemocaoAtivo = false;
//	private VBox cupomFiscalItemsBox;
//	private ScrollPane cupomFiscalScrollPane;
//
//	private TextField tfCodigoProduto;
//	private TextField tfQuantidade;
//	private Stage primaryStage;
//	private Consumer<Funcionario> onReturnToInitialScreen;
//	private ProdutoController produtoController;
//	private Funcionario funcionarioLogado;
//	private FuncionarioController funcionarioController;
//	private VendaController vendaController;
//
//	/**
//	 * Construtor da tela venda. Inicializa os controladores e configura a
//	 * interface.
//	 *
//	 * @param primaryStage            Janela principal da aplicação.
//	 * @param onReturnToInitialScreen Callback para retornar a tela principal.
//	 * @param funcionarioLogado       Funcionário atualmente logado para a
//	 *                                realização da venda.
//	 */
//	public TelaVenda(Stage primaryStage, Consumer<Funcionario> onReturnToInitialScreen,
//                     Funcionario funcionarioLogado, VendaController vendaController) {
//		this.primaryStage = primaryStage;
//		this.onReturnToInitialScreen = onReturnToInitialScreen;
//		this.funcionarioLogado = funcionarioLogado;
//		this.vendaController = vendaController; // ← usa o controller existente
//		this.funcionarioController = new FuncionarioController();
//		this.produtoController = new ProdutoController();
//
//		if (this.funcionarioLogado != null && this.vendaController.getVendaAtual() == null) {
//			this.vendaController.iniciarNovaVenda(this.funcionarioLogado.getIdFuncionario());
//		}
//
//		initializeUI();
//		setupListeners();
//	}
//
//	/**
//	 * Inicializa os componentes visuais da tela de venda,incluindo menu, centro e
//	 * rodapé.
//	 */
//	private void initializeUI() {
//		this.setSpacing(0);
//		this.setStyle("-fx-background-color: #F5F5F5;");
//		MenuBar menuBar = criarMenuBar();
//		HBox centroBox = new HBox();
//		centroBox.setSpacing(10);
//		centroBox.setPadding(new Insets(10));
//		VBox parteEsquerdaBox = criarParteEsquerdaBox();
//		VBox parteDireitaBox = criarParteDireitaBox();
//		HBox.setHgrow(parteEsquerdaBox, Priority.ALWAYS);
//		HBox.setHgrow(parteDireitaBox, Priority.ALWAYS);
//		parteEsquerdaBox.setMinWidth(200);
//		parteEsquerdaBox.setPrefWidth(550);
//		parteDireitaBox.setMinWidth(200);
//		parteDireitaBox.setPrefWidth(450);
//		centroBox.getChildren().addAll(parteEsquerdaBox, parteDireitaBox);
//		HBox footerBox = criarFooterBox();
//		statusModoLabel = new Label("Modo: Adição (F3 para Remoção)");
//		statusModoLabel.setAlignment(Pos.CENTER);
//		statusModoLabel.setMaxWidth(Double.MAX_VALUE);
//		statusModoLabel.setStyle(
//				"-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.5, 0, 1);");
//		this.getChildren().addAll(menuBar, statusModoLabel, centroBox, footerBox);
//
//		VBox.setVgrow(centroBox, Priority.ALWAYS);
//		Platform.runLater(() -> tfCodigoProduto.requestFocus());
//		// limparCamposProdutoAtual("NENHUM PRODUTO SELECIONADO");
//		atualizarTotalCompra();
//
//	}
//
//	/**
//	 * Configura os listeners para realizar alterações na venda e eventos no telcado
//	 */
//	private void setupListeners() {
//		this.vendaController.getVendaAtual().getItensVenda()
//				.addListener((javafx.collections.ListChangeListener.Change<? extends ItemVenda> change) -> {
//					while (change.next()) {
//						if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
//							atualizarTotalCompra();
//							renderizarCupomFiscal();
//						}
//					}
//				});
//
//		this.sceneProperty().addListener((obs, oldScene, newScene) -> {
//			if (newScene != null) {
//				newScene.setOnKeyPressed(event -> {
//					if (event.getCode() == KeyCode.F3) {
//						if (funcionarioLogado != null && funcionarioLogado.getCargo() == Cargo.GERENTE) {
//							alternarModoRemocao();
//						} else {
//							solicitarAutorizacao();
//						}
//						event.consume();
//					} else if (event.getCode() == KeyCode.ESCAPE) {
//						confirmarESair();
//						event.consume();
//					} else if (event.getCode() == KeyCode.F10) {
//						abrirTelaConfirmacaoVenda();
//						event.consume();
//					} else if (event.getCode() == KeyCode.F4) {
//						Alert alert = new Alert(AlertType.CONFIRMATION);
//						alert.setTitle("Confirmar Cancelamento");
//						alert.setHeaderText("Cancelar Venda Completa?");
//						if (this.vendaController.getVendaAtual() != null
//								&& !this.vendaController.getVendaAtual().getItensVenda().isEmpty()) {
//							alert.setContentText(
//									"Esta ação irá remover todos os itens da venda atual. Deseja continuar?");
//						} else {
//							alert.setContentText("Não há itens na venda para cancelar.");
//						}
//						Optional<ButtonType> result = alert.showAndWait();
//						if (result.isPresent() && result.get() == ButtonType.OK) {
//							cancelarVendaCompleta();
//						}
//						event.consume();
//					}
//				});
//			}
//		});
//	}
//
//	private VBox criarParteDireitaBox() {
//		VBox parteDireitaBox = new VBox();
//		// Estilo direto para o painel principal da direita
//		parteDireitaBox.setStyle(
//				"-fx-background-color: white; -fx-padding: 15px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 2);");
//		parteDireitaBox.setSpacing(15);
//		parteDireitaBox.setPadding(new Insets(10));
//
//		// Box para a Quantidade
//		VBox quantidadeBox = new VBox();
//		quantidadeBox.setStyle(
//				"-fx-background-color: #ECEFF1; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px;");
//		quantidadeBox.setMinHeight(70);
//		Label quantidadeLabel = new Label("QUANTIDADE");
//		quantidadeLabel.setStyle("-fx-text-fill: #616161; -fx-font-size: 14px;");
//		quantidadeLabel.setMaxWidth(Double.MAX_VALUE);
//		quantidadeLabel.setAlignment(Pos.TOP_LEFT);
//		quantidadeValorLabel = new Label("0");
//		quantidadeValorLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0D47A1;");
//		quantidadeValorLabel.setMaxWidth(Double.MAX_VALUE);
//		quantidadeValorLabel.setAlignment(Pos.BOTTOM_RIGHT);
//		quantidadeBox.getChildren().addAll(quantidadeLabel, quantidadeValorLabel);
//
//		// Box para o Valor Unitário
//		VBox valorUnitarioBox = new VBox();
//		valorUnitarioBox.setStyle(
//				"-fx-background-color: #ECEFF1; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px;");
//		valorUnitarioBox.setMinHeight(70);
//		Label valorUnitaroLabel = new Label("VALOR UNITARIO");
//		valorUnitaroLabel.setStyle("-fx-text-fill: #616161; -fx-font-size: 14px;");
//		valorUnitaroLabel.setMaxWidth(Double.MAX_VALUE);
//		valorUnitaroLabel.setAlignment(Pos.TOP_LEFT);
//		precoUnitarioLabel = new Label("R$ 0,00");
//		precoUnitarioLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0D47A1;");
//		precoUnitarioLabel.setMaxWidth(Double.MAX_VALUE);
//		precoUnitarioLabel.setAlignment(Pos.BOTTOM_RIGHT);
//		valorUnitarioBox.getChildren().addAll(valorUnitaroLabel, precoUnitarioLabel);
//
//		// Box para o Nome e Total do Item Atual
//		VBox nomeValorBox = new VBox();
//		nomeValorBox.setStyle(
//				"-fx-background-color: #ECEFF1; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 5px;");
//		nomeValorBox.setMinHeight(70);
//		nomeProdutoLabel = new Label("NENHUM PRODUTO SELECIONADO");
//		nomeProdutoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #212121;");
//		nomeProdutoLabel.setMaxWidth(Double.MAX_VALUE);
//		nomeProdutoLabel.setAlignment(Pos.TOP_CENTER);
//		HBox linhaInferiorHBox = new HBox();
//		linhaInferiorHBox.setSpacing(10);
//		linhaInferiorHBox.setMaxWidth(Double.MAX_VALUE);
//		linhaInferiorHBox.setAlignment(Pos.CENTER_LEFT);
//		unidadePrecoLabel = new Label("0X");
//		unidadePrecoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");
//		totalItemAtualLabel = new Label("R$ 0,00");
//		totalItemAtualLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");
//		HBox.setHgrow(totalItemAtualLabel, Priority.ALWAYS);
//		totalItemAtualLabel.setMaxWidth(Double.MAX_VALUE);
//		totalItemAtualLabel.setAlignment(Pos.CENTER_RIGHT);
//		linhaInferiorHBox.getChildren().addAll(unidadePrecoLabel, totalItemAtualLabel);
//		nomeValorBox.getChildren().addAll(nomeProdutoLabel, linhaInferiorHBox);
//
//		// Box para o Total da Compra
//		VBox totalCompraBox = new VBox();
//		totalCompraBox.setStyle(
//				"-fx-background-color: #E65100; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 2);");
//		totalCompraBox.setMinHeight(70);
//		Label valorFinalLabel = new Label("TOTAL DA COMPRA");
//		valorFinalLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
//		valorFinalLabel.setMaxWidth(Double.MAX_VALUE);
//		valorFinalLabel.setAlignment(Pos.CENTER_LEFT);
//
//		totalCompraLabel = new Label("R$ 0,00");
//		totalCompraLabel.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");
//		totalCompraLabel.setMaxWidth(Double.MAX_VALUE);
//		totalCompraLabel.setAlignment(Pos.CENTER_RIGHT);
//		totalCompraBox.getChildren().addAll(valorFinalLabel, totalCompraLabel);
//
//		// Box para o Input de Código de Barras
//		VBox inputCodigoBarrasBox = new VBox();
//		inputCodigoBarrasBox.setStyle(
//				"-fx-background-color: #ECEFF1; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 5px;");
//		inputCodigoBarrasBox.setMinHeight(70);
//		Label codigoBarrasLabel = new Label("CÓDIGO DE BARRAS / PRODUTO");
//		codigoBarrasLabel.setStyle("-fx-text-fill: #616161; -fx-font-size: 14px;");
//		codigoBarrasLabel.setMaxWidth(Double.MAX_VALUE);
//		codigoBarrasLabel.setAlignment(Pos.TOP_LEFT);
//		tfCodigoProduto = new TextField();
//		tfCodigoProduto.setPromptText("Digite o código do produto ou use o leitor");
//		tfCodigoProduto.setStyle("-fx-font-size: 14px;");
//
//		// Definir quantidade
//		Label quantidadeLabelAdd = new Label("QUANTIDADE");
//		quantidadeLabelAdd.setStyle("-fx-text-fill: #616161; -fx-font-size: 14px;");
//		tfQuantidade = new TextField("1");
//		tfQuantidade.setStyle("-fx-font-size: 14px;");
//		tfQuantidade.setMaxWidth(80);
//
//		tfCodigoProduto.setOnAction(event -> {
//			String codigoBarras = tfCodigoProduto.getText().trim();
//			int quantidade = 1;
//			try {
//				quantidade = Integer.parseInt(tfQuantidade.getText().trim());
//				if (quantidade <= 0)
//					quantidade = 1;
//			} catch (NumberFormatException e) {
//				quantidade = 1;
//			}
//
//			if (codigoBarras.isEmpty()) {
//				limparCamposProdutoAtual("NENHUM PRODUTO SELECIONADO");
//				tfCodigoProduto.clear();
//				return;
//			}
//
//			Produto produto = produtoController.buscarPorCodigo(codigoBarras);
//
//			if (produto != null) {
//				if (modoRemocaoAtivo) {
//					removerItemDaVenda(codigoBarras, quantidade);
//				} else {
//					Double estoqueAtual = produto.getQuantidadeOuPesoEmEstoque();
//					if (quantidade > estoqueAtual) {
//						nomeProdutoLabel.setText("ESTOQUE INSUFICIENTE!");
//						return;
//					}
//					double precoOriginal = produto.getPrecoVenda();
//					double desconto = produto.getDesconto();
//					double precoComDesconto = precoOriginal * (1 - desconto);
//
//					precoUnitarioLabel.setText(String.format("R$ %.2f", precoComDesconto));
//					unidadePrecoLabel.setText(quantidade + "X");
//					totalItemAtualLabel.setText(String.format("R$ %.2f", precoComDesconto * quantidade));
//
//					for (int i = 0; i < quantidade; i++) {
//						processarProdutoEscaneado(produto);
//					}
//				}
//			} else {
//				limparCamposProdutoAtual("PRODUTO NÃO ENCONTRADO");
//			}
//
//			tfCodigoProduto.clear();
//			tfQuantidade.setText("1");
//		});
//
//		inputCodigoBarrasBox.getChildren().addAll(quantidadeLabelAdd, tfQuantidade, codigoBarrasLabel, tfCodigoProduto);
//		VBox.setMargin(inputCodigoBarrasBox, new Insets(0, 0, 10, 0));
//		parteDireitaBox.getChildren().addAll(quantidadeBox, valorUnitarioBox, nomeValorBox, totalCompraBox,
//				inputCodigoBarrasBox);
//		return parteDireitaBox;
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	private HBox criarFooterBox() {
//		HBox footerBox = new HBox();
//		footerBox.setPadding(new Insets(10));
//		footerBox.setSpacing(10);
//		footerBox.setAlignment(Pos.CENTER_LEFT);
//		footerBox.setStyle("-fx-background-color: #4A4A4A;");
//
//		Label empresaFooterLabel = new Label("MERCADINHO PROVIDENCE");
//		empresaFooterLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
//
//		Label atendenteFooterLabel = new Label(
//				"ATENDENTE: " + (funcionarioLogado != null ? funcionarioLogado.getNome() : "N/A"));
//		atendenteFooterLabel.setStyle("-fx-text-fill: #BDBDBD; -fx-font-size: 12px;");
//		atendenteFooterLabel.setAlignment(Pos.CENTER);
//
//		VBox menuAtalhosFooterBox = new VBox();
//		menuAtalhosFooterBox.setAlignment(Pos.CENTER);
//
//		Label menuFooterLabel = new Label("Menu: Teclas de atalho");
//		menuFooterLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
//
//		Label f1FooterLabel = new Label("pressione = [F1]");
//		f1FooterLabel.setStyle("-fx-text-fill: #FFCC80; -fx-font-size: 12px; -fx-font-weight: bold;");
//		menuAtalhosFooterBox.getChildren().addAll(menuFooterLabel, f1FooterLabel);
//
//		Label horasFooterLabel = new Label();
//		horasFooterLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
//		horasFooterLabel.setAlignment(Pos.CENTER);
//		TimeUtils.updateDateTime(horasFooterLabel);
//
//		Region spacerLeftFooter = new Region();
//		HBox.setHgrow(spacerLeftFooter, Priority.ALWAYS);
//		Region spacerCenterFooter = new Region();
//		HBox.setHgrow(spacerCenterFooter, Priority.ALWAYS);
//		Region spacerRightFooter = new Region();
//		HBox.setHgrow(spacerRightFooter, Priority.ALWAYS);
//
//		footerBox.getChildren().addAll(empresaFooterLabel, spacerLeftFooter, atendenteFooterLabel, menuAtalhosFooterBox,
//				spacerCenterFooter, spacerRightFooter, horasFooterLabel);
//		return footerBox;
//	}
//
//	private void renderizarCupomFiscal() {
//		cupomFiscalItemsBox.getChildren().clear();
//
//		ColumnConstraints colQtd = new ColumnConstraints();
//		colQtd.setPrefWidth(50);
//		colQtd.setMinWidth(50);
//		colQtd.setMaxWidth(50);
//		colQtd.setHalignment(HPos.CENTER);
//
//		ColumnConstraints colDesc = new ColumnConstraints();
//		colDesc.setHgrow(Priority.ALWAYS);
//		colDesc.setMinWidth(150);
//		colDesc.setHalignment(HPos.LEFT);
//
//		ColumnConstraints colUnit = new ColumnConstraints();
//		colUnit.setPrefWidth(70);
//		colUnit.setHalignment(HPos.RIGHT);
//
//		ColumnConstraints colTotal = new ColumnConstraints();
//		colTotal.setPrefWidth(90);
//		colTotal.setHalignment(HPos.RIGHT);
//
//		GridPane headerGrid = new GridPane();
//		headerGrid.setHgap(10);
//		headerGrid.setAlignment(Pos.CENTER_LEFT);
//		headerGrid.getStyleClass().add("receipt-header-grid");
//		headerGrid.getColumnConstraints().addAll(colQtd, colDesc, colUnit, colTotal);
//
//		Label headerQtd = new Label("QTD");
//		headerGrid.add(headerQtd, 0, 0);
//
//		Label headerDesc = new Label("NOME");
//		headerGrid.add(headerDesc, 1, 0);
//
//		Label headerUnit = new Label("UNIT.");
//		headerGrid.add(headerUnit, 2, 0);
//
//		Label headerTotal = new Label("TOTAL");
//		headerGrid.add(headerTotal, 3, 0);
//
//		cupomFiscalItemsBox.getChildren().add(headerGrid);
//
//		for (ItemVenda item : this.vendaController.getVendaAtual().getItensVenda()) {
//			GridPane itemGrid = new GridPane();
//			itemGrid.setHgap(10);
//			itemGrid.setAlignment(Pos.CENTER_LEFT);
//			itemGrid.getStyleClass().add("receipt-item-grid");
//
//			ColumnConstraints itemColQtd = new ColumnConstraints();
//			itemColQtd.setPrefWidth(50);
//			itemColQtd.setMinWidth(50);
//			itemColQtd.setMaxWidth(50);
//			itemColQtd.setHalignment(HPos.CENTER);
//
//			ColumnConstraints itemColDesc = new ColumnConstraints();
//			itemColDesc.setHgrow(Priority.ALWAYS);
//			itemColDesc.setMinWidth(150);
//			itemColDesc.setHalignment(HPos.LEFT);
//
//			ColumnConstraints itemColUnit = new ColumnConstraints();
//			itemColUnit.setPrefWidth(70);
//			itemColUnit.setHalignment(HPos.RIGHT);
//
//			ColumnConstraints itemColTotal = new ColumnConstraints();
//			itemColTotal.setPrefWidth(90);
//			itemColTotal.setHalignment(HPos.RIGHT);
//
//			itemGrid.getColumnConstraints().addAll(itemColQtd, itemColDesc, itemColUnit, itemColTotal);
//
//			Label qtdLabel = new Label(String.valueOf(item.getQuantidadeOuPeso()));
//			qtdLabel.getStyleClass().add("label");
//			qtdLabel.setAlignment(Pos.CENTER);
//			itemGrid.add(qtdLabel, 0, 0);
//
//			Label nomeLabel = new Label(item.getProduto().getNome().toUpperCase());
//			nomeLabel.setWrapText(true);
//			nomeLabel.getStyleClass().add("label");
//			itemGrid.add(nomeLabel, 1, 0);
//
//			Label unitLabel = new Label(String.format("R$ %.2f", item.getPrecoUnitarioVenda()));
//			unitLabel.getStyleClass().add("label");
//			itemGrid.add(unitLabel, 2, 0);
//
//			Label totalItemLabel = new Label(String.format("R$ %.2f", item.getTotalItem()));
//			totalItemLabel.getStyleClass().addAll("label", "total-item");
//			itemGrid.add(totalItemLabel, 3, 0);
//
//			cupomFiscalItemsBox.getChildren().add(itemGrid);
//		}
//
//		Label separatorLine = new Label("--------------------------------------------------");
//		separatorLine.setMaxWidth(Double.MAX_VALUE);
//		separatorLine.setAlignment(Pos.CENTER);
//		separatorLine.getStyleClass().add("receipt-separator-line");
//		cupomFiscalItemsBox.getChildren().add(separatorLine);
//
//		cupomFiscalScrollPane.layout();
//		cupomFiscalScrollPane.setVvalue(1.0);
//	}
//
//	private void alternarModoRemocao() {
//		modoRemocaoAtivo = !modoRemocaoAtivo;
//		if (modoRemocaoAtivo) {
//			statusModoLabel.setText("MODO: REMOÇÃO ATIVO (F3 para Adição)");
//			statusModoLabel.getStyleClass().remove("status-label-adicao");
//			statusModoLabel.getStyleClass().add("status-label-remocao");
//			System.out.println("Modo de Remoção Ativo.");
//		} else {
//			statusModoLabel.setText("Modo: Adição (F3 para Remoção)");
//			statusModoLabel.getStyleClass().remove("status-label-remocao");
//			statusModoLabel.getStyleClass().add("status-label-adicao");
//			System.out.println("Modo de Adição Ativo.");
//		}
//		tfCodigoProduto.clear();
//		limparCamposProdutoAtual("NENHUM PRODUTO SELECIONADO");
//	}
//
//	private VBox criarParteEsquerdaBox() {
//		VBox parteEsquerdaBox = new VBox();
//		parteEsquerdaBox.setStyle(
//				"-fx-background-color: white; -fx-padding: 15px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 2);");
//
//		Label tituloCupomLabel = new Label("RECIBO DA COMPRA");
//		tituloCupomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E65100;");
//
//		cupomFiscalItemsBox = new VBox();
//		cupomFiscalItemsBox.setSpacing(5);
//		cupomFiscalItemsBox.getStyleClass().add("receipt-items-box");
//
//		cupomFiscalScrollPane = new ScrollPane(cupomFiscalItemsBox);
//		cupomFiscalScrollPane.setFitToWidth(true);
//		cupomFiscalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//		cupomFiscalScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//		cupomFiscalScrollPane.getStyleClass().add("receipt-scroll-pane");
//
//		VBox.setVgrow(cupomFiscalScrollPane, Priority.ALWAYS);
//		parteEsquerdaBox.getChildren().addAll(tituloCupomLabel, cupomFiscalScrollPane);
//
//		return parteEsquerdaBox;
//	}
//
//	private void processarProdutoEscaneado(Produto produto) {
//		Venda vendaAtual = this.vendaController.getVendaAtual();
//
//		Optional<ItemVenda> itemVendaExistenteOptional = vendaAtual.getItensVenda().stream()
//				.filter(item -> item.getProduto() != null
//						&& item.getProduto().getCodigoDeBarras().equals(produto.getCodigoDeBarras()))
//				.findFirst();
//
//		ItemVenda itemExistente = itemVendaExistenteOptional.orElse(null);
//		Double quantidadeAtualNoCarrinho = (itemExistente != null) ? itemExistente.getQuantidadeOuPeso() : 0;
//		Double quantidadeAdicionar = 1.0;
//		Double proximaQuantidadeNoCarrinho = quantidadeAtualNoCarrinho + quantidadeAdicionar;
//		Double estoqueDisponivel = produto.getQuantidadeOuPesoEmEstoque();
//
//		if (proximaQuantidadeNoCarrinho > estoqueDisponivel) {
//
//			Optional<ButtonType> result = AlertUtils.showConfirmationAndGetResult("Produto excedido em nosso estoque.",
//					"O produto \"" + produto.getNome() + "\" excede o estoque disponível.",
//					String.format(
//							"Estoque atual: %d\nQuantidade desejada: %d\n\nDeseja adicioná-lo mesmo assim (requer autorização)?",
//							estoqueDisponivel, proximaQuantidadeNoCarrinho));
//
//			if (result.isPresent() && result.get() == ButtonType.OK) {
//				Double quantidadeParaAddComAutorizacao = solicitarAutorizacaoEQuantidade(produto, estoqueDisponivel,
//						proximaQuantidadeNoCarrinho);
//
//				if (quantidadeParaAddComAutorizacao > 0) {
//					if (itemExistente != null) {
//						itemExistente.setQuantidadeOuPeso(itemExistente.getQuantidadeOuPeso() + quantidadeParaAddComAutorizacao);
//					} else {
//						ItemVenda novoItem = new ItemVenda(produto, quantidadeParaAddComAutorizacao);
//						this.vendaController.adicionarItemAVenda(novoItem);
//					}
//
//					atualizarTotalCompra();
//					renderizarCupomFiscal();
//
//					nomeProdutoLabel.setText(produto.getNome().toUpperCase());
//					quantidadeValorLabel.setText(String.valueOf(quantidadeParaAddComAutorizacao));
//					precoUnitarioLabel.setText(String.format("R$ %.2f", produto.getPrecoVenda()));
//					unidadePrecoLabel.setText(String.format("%dX", quantidadeParaAddComAutorizacao));
//					totalItemAtualLabel.setText(String.format("R$ %.2f",
//							(double) quantidadeParaAddComAutorizacao * produto.getPrecoVenda()));
//					return;
//				} else {
//					AlertUtils.showInfo("Operação Cancelada", "Adição de produto cancelada.");
//					limparCamposProdutoAtual("NENHUM PRODUTO SELECIONADO");
//					return;
//				}
//			} else {
//				AlertUtils.showInfo("Operação Cancelada", "Adição de produto cancelada.");
//				limparCamposProdutoAtual("NENHUM PRODUTO SELECIONADO");
//				return;
//			}
//		}
//
//		if (itemExistente != null) {
//			itemExistente.setQuantidadeOuPeso(itemExistente.getQuantidadeOuPeso() + 1);
//		} else {
//			ItemVenda novoItem = new ItemVenda(produto, quantidadeAdicionar);
//			this.vendaController.adicionarItemAVenda(novoItem);
//		}
//
//		atualizarTotalCompra();
//		renderizarCupomFiscal();
//
//		Optional<ItemVenda> itemAtualizadoNoCarrinho = vendaAtual.getItensVenda().stream()
//				.filter(item -> item.getProduto() != null
//						&& item.getProduto().getCodigoDeBarras().equals(produto.getCodigoDeBarras()))
//				.findFirst();
//
//		if (itemAtualizadoNoCarrinho.isPresent()) {
//			ItemVenda item = itemAtualizadoNoCarrinho.get();
//			nomeProdutoLabel.setText(item.getProduto().getNome().toUpperCase());
//			quantidadeValorLabel.setText(String.valueOf(item.getQuantidadeOuPeso()));
//			precoUnitarioLabel.setText(String.format("R$ %.2f", item.getProduto().getPrecoVenda()));
//			unidadePrecoLabel.setText(String.format("R$ %.2f", item.getQuantidadeOuPeso()));
//			totalItemAtualLabel.setText(String.format("R$ %.2f", item.getTotalItem()));
//		}
//	}
//
//	private Double solicitarAutorizacaoEQuantidade(Produto produto, Double estoqueDisponivel, Double quantidadeDesejada) {
//		TextInputDialog dialog = new TextInputDialog(String.valueOf(quantidadeDesejada));
//		dialog.setTitle("Autorização Necessária");
//		dialog.setHeaderText("O produto \"" + produto.getNome() + "\" excede o estoque disponível.");
//		dialog.setContentText(String.format(
//				"Estoque atual: %d.\nInforme a quantidade para adicionar (requer autorização):", estoqueDisponivel));
//
//		// Adiciona um TextField para o funcionário autorizador
//		TextField autorizadorField = new TextField();
//		autorizadorField.setPromptText("Nome do autorizador (gerente, supervisor)");
//		VBox dialogContent = new VBox(10, dialog.getEditor(), autorizadorField);
//		dialog.getDialogPane().setContent(dialogContent);
//
//		Platform.runLater(() -> dialog.getEditor().requestFocus()); // Foca no campo de quantidade
//
//		Optional<String> result = dialog.showAndWait();
//		if (result.isPresent()) {
//			try {
//				Double quantidadeDigitada = Double.parseDouble(result.get());
//				String nomeAutorizador = autorizadorField.getText().trim();
//
//				if (quantidadeDigitada <= 0) {
//					AlertUtils.showWarning("Quantidade Inválida", "A quantidade deve ser maior que zero.");
//					return 0.0;
//				}
//				if (nomeAutorizador.isEmpty()) {
//					AlertUtils.showWarning("Autorização Necessária", "Por favor, informe o nome do autorizador.");
//					return 0.0;
//				}
//				System.out.println("Autorização de " + nomeAutorizador + " para adicionar " + quantidadeDigitada
//						+ " unidades de " + produto.getNome());
//				return quantidadeDigitada;
//
//			} catch (NumberFormatException e) {
//				AlertUtils.showError("Entrada Inválida", "Por favor, digite uma quantidade numérica válida.");
//				return 0.0;
//			}
//		}
//		return 0.0;
//	}
//
//	private MenuBar criarMenuBar() {
//		MenuBar menuBar = new MenuBar();
//		menuBar.setStyle("-fx-background-color: #E65100;");
//
//		Menu ajudaMenu = new Menu("Ajuda");
//		ajudaMenu.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
//		Menu opcoesVendaMenu = new Menu("Opções de Venda");
//		opcoesVendaMenu.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
//		Menu voltarMenu = new Menu("Voltar");
//		voltarMenu.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
//
//		MenuItem atalhosVendaItem = new MenuItem("Atalhos de Venda (F1)");
//		atalhosVendaItem.setOnAction(e -> mostrarAjudaVenda());
//		ajudaMenu.getItems().add(atalhosVendaItem);
//
//		MenuItem cancelarVendaMenuItem = new MenuItem("Cancelar compra (F4)");
//		cancelarVendaMenuItem.setOnAction(e -> {
//			cancelarVendaCompleta();
//		});
//
//		MenuItem finalizarVendaMenuItem = new MenuItem("Finalizar Venda (F10)");
//		finalizarVendaMenuItem.setOnAction(e -> {
//			abrirTelaConfirmacaoVenda();
//		});
//		opcoesVendaMenu.getItems().addAll(cancelarVendaMenuItem, finalizarVendaMenuItem);
//
//		MenuItem voltarMenuItem = new MenuItem("Sair da Venda (ESC)");
//		voltarMenuItem.setOnAction(e -> {
//			confirmarESair();
//		});
//		voltarMenu.getItems().add(voltarMenuItem);
//
//		menuBar.getMenus().addAll(ajudaMenu, opcoesVendaMenu, voltarMenu);
//
//		return menuBar;
//	}
//
//	/**
//	 * Função responsável por remover determinado item da lista de vendas.
//	 *
//	 * @param codigoBarras
//	 * @param quantidadeSolicitada
//	 */
//	private void removerItemDaVenda(String codigoBarras, int quantidadeSolicitada) {
//		Double quantidadeDisponivel = vendaController.getQuantidadeItemNaVenda(codigoBarras);
//
//		if (quantidadeDisponivel == 0) {
//			System.out.println("Produto com código " + codigoBarras + " não está na venda.");
//			limparCamposProdutoAtual("PRODUTO NÃO ENCONTRADO NA VENDA!");
//			return;
//		}
//
//		if (quantidadeSolicitada > quantidadeDisponivel) {
//			System.out.println("Quantidade solicitada (" + quantidadeSolicitada + ") excede o disponível ("
//					+ quantidadeDisponivel + ").");
//			limparCamposProdutoAtual("QUANTIDADE EXCEDE O DISPONÍVEL!");
//			return;
//		}
//
//		int removidos = 0;
//		for (int i = 0; i < quantidadeSolicitada; i++) {
//			boolean itemRemovido = vendaController.removerItemDaVenda(codigoBarras);
//			if (itemRemovido) {
//				removidos++;
//			}
//		}
//
//		System.out.println(removidos + " item(s) com código " + codigoBarras + " removido(s) da venda.");
//		atualizarTotalCompra();
//		limparCamposProdutoAtual(removidos + " ITEM(S) REMOVIDO(S)");
//	}
//
//	/**
//	 * Função reponsável por abir a dialog de sair da tela de vendas.
//	 */
//	private void confirmarESair() {
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Confirmação de Saída");
//		alert.setHeaderText("Sair da Tela de Venda?");
//
//		if (this.vendaController.getVendaAtual() != null
//				&& !this.vendaController.getVendaAtual().getItensVenda().isEmpty()) {
//			alert.setContentText("Existem itens na venda atual. Deseja sair e descartar esta venda?");
//		} else {
//			alert.setContentText("Deseja sair da tela de venda?");
//		}
//
//		Optional<ButtonType> result = alert.showAndWait();
//		if (result.isPresent() && result.get() == ButtonType.OK) {
//			if (this.vendaController.getVendaAtual() != null
//					&& !this.vendaController.getVendaAtual().getItensVenda().isEmpty()) {
//				this.vendaController.cancelarVenda();
//			}
//			if (onReturnToInitialScreen != null) {
//				onReturnToInitialScreen.accept(funcionarioLogado);
//			}
//		}
//	}
//
//	/**
//	 * Função responsável por atualizar o total da compra.
//	 */
//	private void atualizarTotalCompra() {
//		totalCompraLabel.setText(String.format("R$ %.2f", this.vendaController.getVendaAtual().getValorTotal()));
//	}
//
//	/**
//	 * Função responsável por abrir a dialog de finalizar a venda.
//	 */
//	private void abrirTelaConfirmacaoVenda() {
//		if (this.vendaController.getVendaAtual() == null
//				|| this.vendaController.getVendaAtual().getItensVenda().isEmpty()) {
//			AlertUtils.showWarning("Venda Vazia", "Não é possível finalizar uma venda sem itens.");
//			return;
//		}
//
//		// Cria a nova instância do Dialog
//		TelaConfirmacaoVendaPagamentoView telaPagamento = new TelaConfirmacaoVendaPagamentoView(primaryStage,
//				this.vendaController);
//
//		// Exibe o diálogo e espera o resultado
//		Optional<Venda> resultado = telaPagamento.showAndWait();
//
//		// Este bloco só é executado DEPOIS que o diálogo é fechado.
//		if (resultado.isPresent()) {
//			System.out.println("✅ Venda finalizada com sucesso!");
//			vendaController.iniciarNovaVenda(funcionarioLogado.getIdFuncionario());
//			limparInterfacePosVenda();
//			renderizarCupomFiscal();
//		} else {
//			System.out.println("❌ Venda não foi finalizada. Mantendo os itens no carrinho.");
//		}
//	}
//
//	private void limparInterfacePosVenda() {
//		limparCamposProdutoAtual("NENHUM PRODUTO SELECIONADO");
//
//		// Zera o total da compra
//		totalCompraLabel.setText("R$ 0,00");
//
//		// Se quiser garantir que o campo de código de barras esteja limpo
//		tfCodigoProduto.clear();
//
//		// Limpa o cupom fiscal visualmente
//		renderizarCupomFiscal();
//	}
//
//	/**
//	 * Função para cancelar uma venda, limpar todos os campos, retirar todos os
//	 * itens da lista e alterar o modo
//	 */
//	private void cancelarVendaCompleta() {
//		this.vendaController.cancelarVenda();
//		limparCamposProdutoAtual("NENHUM PRODUTO SELECIONADO");
//		if (modoRemocaoAtivo) {
//			alternarModoRemocao();
//		}
//	}
//
//	/**
//	 *
//	 */
//	private void limparCamposProdutoAtual(String message) {
//		nomeProdutoLabel.setText(message);
//		quantidadeValorLabel.setText("0");
//		precoUnitarioLabel.setText("R$ 0,00");
//		unidadePrecoLabel.setText("0X");
//		totalItemAtualLabel.setText("R$ 0,00");
//		tfCodigoProduto.clear();
//	}
//
//	private void mostrarAjudaVenda() {
//		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setTitle("Ajuda - Atalhos de Venda");
//		alert.setHeaderText("Teclas de atalho disponíveis para agilizar a operação:");
//
//		VBox conteudoBox = new VBox(5);
//		conteudoBox.setPadding(new Insets(10));
//
//		String[] atalhos = { "🔹 F3 – Alternar entre modo Adição/Remoção", "🔹 F4 – Cancelar compra atual",
//				"🔹 F10 – Finalizar venda", "🔹 ESC – Sair da tela de venda", "🔹 ENTER – Confirmar produto",
//				"🔹 TAB – Navegar entre campos" };
//
//		for (String atalho : atalhos) {
//			Label label = new Label(atalho);
//			label.setStyle("-fx-font-size: 14px;");
//			conteudoBox.getChildren().add(label);
//		}
//
//		alert.getDialogPane().setContent(conteudoBox);
//		alert.showAndWait();
//	}
//
//	private void solicitarAutorizacao() {
//		if (modoRemocaoAtivo) {
//			alternarModoRemocao();
//			return;
//		}
//
//		TextInputDialog dialog = new TextInputDialog();
//		dialog.setTitle("Autorização Necessária");
//		dialog.setHeaderText("Para entrar no modo de remoção, informe o código de um gerente.");
//		dialog.setContentText("Código do Gerente:");
//
//		Optional<String> result = dialog.showAndWait();
//
//		if (result.isPresent()) {
//			String codigoDigitado = result.get().trim();
//
//			Funcionario gerente = funcionarioController.buscarFuncionarioPorCodigo(codigoDigitado);
//
//			if (gerente != null && gerente.getCargo() == Cargo.GERENTE) {
//				// Código correto, permite a alteração do modo
//				alternarModoRemocao();
//				AlertUtils.showInfo("Autorizado", "Modo de remoção ativado com sucesso!");
//			} else {
//				// Código incorreto
//				AlertUtils.showError("Acesso Negado", "Código de gerente inválido.");
//			}
//		}
//
//	}
//
//}