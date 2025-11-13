package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.dto.LoginResponseDto;
import br.com.mercadinhoprovidence.util.Timeutils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TelaInicialView {

    private final MainApplication mainApplication;
    private final LoginResponseDto funcionarioLogado;
    /**
     * -- GETTER --
     *  Retorna a Scene encapsulada por esta view
     *
     */
    @Getter
    private Scene scene;

    private final Map<String, String> botoesConfig = new LinkedHashMap<>();
    private final Map<String, Supplier<StackPane>> telasConfig = new LinkedHashMap<>();
    private final Set<String> botoesDesabilitados;

    private StackPane centerPane;
    private Label userInfo;

    private Consumer<LoginResponseDto> onOpenPdvScreen;

    /**
     * COnstrutor para a tela inicial View.
     *
     * @param mainApplication     A instância da aplicação principal para navegação.
     * @param funcionarioLogado   O funcionário que realizou o login.
     * @param botoesDesabilitados Conjunto de textos e botões que devem ser
     *                            desabilitados.
     */
    public TelaInicialView(MainApplication mainApplication, LoginResponseDto funcionarioLogado,
                           Set<String> botoesDesabilitados, Consumer<LoginResponseDto> onOpenPdvSceen) {
        if (mainApplication == null || funcionarioLogado == null) {
            throw new IllegalArgumentException("MainApplication e FuncionarioLogado não podem ser nulos.");
        }
        this.mainApplication = mainApplication;
        this.funcionarioLogado = funcionarioLogado;
        this.botoesDesabilitados = botoesDesabilitados != null ? botoesDesabilitados : new HashSet<>();
        this.onOpenPdvScreen = onOpenPdvSceen;
        initializeViewData();
        setupUI();
    }

    /**
     * Inicializa os maps de configurações de botões e telas.
     */
    private void initializeViewData() {
        // Adicionando as telas nos botões
        //telasConfig.put("Estoque", () -> new EstoqueView(mainApplication, funcionarioLogado).getView());
        telasConfig.put("Relatorio", RelatorioView::getView);
        //telasConfig.put("Funcionarios", () -> new FuncionariosView(mainApplication, funcionarioLogado).getView());
        //telasConfig.put("Ajuda", () -> new EstoqueView(mainApplication, funcionarioLogado).getView());

        // Adicionando btns (caminhos dos ícones)
        botoesConfig.put("PDV", "/images/carinho.png");
        botoesConfig.put("Estoque", "/images/estoque.png");
        botoesConfig.put("Relatorio", "/images/grafico.png");
        botoesConfig.put("Funcionarios", "/images/funcionarios.png");
        botoesConfig.put("Ajuda", "/images/ajuda.png");
        botoesConfig.put("Sair", "/images/sair.png");
    }

    /***/
    private void setupUI() {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setLeft(createSidebar());

        centerPane = new StackPane();
        centerPane.getStyleClass().add("center-pane");
        centerPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label welcomeContentLabel = new Label(
                "Bem-vindo ao Mercadinho Providence, " + funcionarioLogado.getName() + "!");
        welcomeContentLabel.getStyleClass().add("welcome-label");

        centerPane.getChildren().add(welcomeContentLabel);
        root.setCenter(centerPane);

        this.scene = new Scene(root);
        configurarAtalhosDeTeclado();
        this.scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/TelaInicial.css")).toExternalForm());    }

    /**
     * Cria e retorna o painel do cabeçalho superior.
     * Agora retorna um BorderPane diretamente para ocupar a largura total.
     *
     * @return BorderPane que representa o cabeçalho.
     */
    private BorderPane createHeader() {

        BorderPane topPane = new BorderPane();
        topPane.getStyleClass().add("header-pane");

        Label titleLabel = new Label("Mercadinho Providence");
        titleLabel.getStyleClass().add("header-title");

        userInfo = new Label("Funcionário logado: " + this.funcionarioLogado.getName().toUpperCase());
        userInfo.getStyleClass().add("user-info-label");

        Label hourLabel = new Label();
        hourLabel.getStyleClass().add("hour-label");
        Timeutils.updateDateTime(hourLabel);

        // HBOx para armazenar usuário e hora
        HBox userInfoAndHourBox = new HBox(20);
        userInfoAndHourBox.setAlignment(Pos.CENTER_RIGHT);
        userInfoAndHourBox.getChildren().addAll(userInfo, hourLabel);

        topPane.setLeft(titleLabel);
        topPane.setRight(userInfoAndHourBox);

        return topPane;
    }

    /**
     * Cria um botão de menu com ícone e texto para a sidebar.
     *
     * @param texto        O texto a ser exibido no botão.
     * @param caminhoIcone O caminho relativo para o arquivo de imagem do ícone.
     * @return O botão configurado.
     */
    private Button createSidebarButton(String texto, String caminhoIcone) {
        Button btn = new Button(texto);
        btn.getStyleClass().add("sidebar-button");
        if (caminhoIcone != null && !caminhoIcone.trim().isEmpty()) {
            try {
                java.net.URL resourceUrl = getClass().getResource(caminhoIcone);

                if (resourceUrl != null) {
                    ImageView icon = new ImageView(new Image(resourceUrl.toExternalForm()));
                    icon.setFitHeight(24);
                    icon.setFitWidth(24);
                    icon.setPreserveRatio(true);
                    btn.setGraphic(icon);
                } else {
                    System.err.println("Aviso: Ícone não encontrado em '" + caminhoIcone + "' para o botão '" + texto + "'.");
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Erro ao carregar ícone para '" + texto + "': " + caminhoIcone + " - " + e.getMessage());
            }
        }
        handleButtonClick(texto, btn);
        return btn;
    }

    /**
     * Configura a ação a ser executada quando um botão da sidebar é clicado.
     *
     * @param texto O texto do botão para identificar a ação.
     * @param btn   O botão a ser configurado.
     */
    private void handleButtonClick(String texto, Button btn) {
        if (texto.equalsIgnoreCase("PDV")) {
            btn.setOnAction(e -> {
                if (onOpenPdvScreen != null) {
                    onOpenPdvScreen.accept(this.funcionarioLogado);
                } else {
                    System.err.println("Erro: Callback onOpenPdvScreen não definido para PDV.");

                }
            });
            return;
        }
        if (texto.equalsIgnoreCase("Sair")) {
            // btn.setOnAction(e -> mainApplication.fecharAplicacao());
            return;
        }
        Supplier<StackPane> view = telasConfig.get(texto);
        if (view != null) {
            btn.setOnAction(e -> {
                StackPane novaTela = view.get();
                centerPane.getChildren().clear();
                centerPane.getChildren().add(novaTela);
            });
        } else {
            System.out.println("DEBUG: Nenhuma view mapeada para: " + texto);
        }
    }

    /**
     * Cria e retorna o painel da barra lateral (menu).
     *
     * @return VBox que representa a barra lateral.
     */
    private VBox createSidebar() {
        VBox sidebarVBox = new VBox(20);
        sidebarVBox.getStyleClass().add("sidebar-pane");
        sidebarVBox.setPrefWidth(250);
        // --- Logo do Mercadinho ---
        try {
            String logoResourcePath = "images/logoMercado.png";

            // Tenta carregar o recurso
            java.net.URL imageUrl = ClassLoader.getSystemResource(logoResourcePath);
            if (imageUrl != null) {
                Image logoImage = new Image(imageUrl.toExternalForm());
                ImageView imageLogoView = new ImageView(logoImage);
                imageLogoView.setFitWidth(250);
                imageLogoView.setPreserveRatio(true);
                VBox.setMargin(imageLogoView, new Insets(0, 0, 0, 0));
                sidebarVBox.getChildren().add(imageLogoView);
            } else {
                throw new IllegalArgumentException("Recurso não encontrado: " + logoResourcePath);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            Label logoPlaceholder = new Label("Logo Aqui");
            logoPlaceholder.getStyleClass().add("logo-placeholder");
            VBox.setMargin(logoPlaceholder, new Insets(0, 0, 20, 0));
            sidebarVBox.getChildren().add(logoPlaceholder);
        }
        // --- Botões do Menu ---
        botoesConfig.forEach((texto, caminhoIcone) -> {
            if (!texto.equalsIgnoreCase("Sair")) {
                Button btn = createSidebarButton(texto, caminhoIcone);
                if (botoesDesabilitados.contains(texto)) {
                    btn.setDisable(true);
                }
                sidebarVBox.getChildren().add(btn);
            }
        });
        // Espaçador para empurrar o botão "Sair" para o final da sidebar
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebarVBox.getChildren().add(spacer);

        Button btnSair = createSidebarButton("Sair", botoesConfig.get("Sair"));
        sidebarVBox.getChildren().add(btnSair);

        return sidebarVBox;
    }

    /**
     *
     * */
    private void configurarAtalhosDeTeclado() {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case F1 -> carregarTela("Estoque");
                case F2 -> carregarTela("Relatorio");
                case F3 -> carregarTela("Funcionarios");
                case F4 -> carregarTela("Ajuda");
                case F5 -> {
                    if (onOpenPdvScreen != null) {
                        onOpenPdvScreen.accept(funcionarioLogado);
                    }
                }
                // case ESCAPE -> mainApplication.fecharAplicacao();
            }
        });
    }

    /**
     * */
    private void carregarTela(String nomeTela) {
        Supplier<StackPane> view = telasConfig.get(nomeTela);
        if (view != null) {
            StackPane novaTela = view.get();
            centerPane.getChildren().setAll(novaTela);
        } else {
            System.out.println("DEBUG: Nenhuma tela mapeada para: " + nomeTela);
        }
    }

}