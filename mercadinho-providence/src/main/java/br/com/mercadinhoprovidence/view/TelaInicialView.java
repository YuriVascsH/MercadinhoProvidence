package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.util.ImagemUtil;
import br.com.mercadinhoprovidence.view.component.DangerSideBarButton;
import br.com.mercadinhoprovidence.view.component.HeaderBar;
import br.com.mercadinhoprovidence.view.component.LogoLabel;
import br.com.mercadinhoprovidence.view.component.SideBarButton;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TelaInicialView extends JPanel {

    private final MainApplication mainApplication;
    private final LoginResponseDto funcionarioLogado;

    private final Map<String, String> botoesConfig = new LinkedHashMap<>();
    private final Map<String, Supplier<JPanel>> telasConfig = new LinkedHashMap<>();
    private final Set<String> botoesDesabilitados;
    private final Map<String, JButton> botoesInstanciados = new HashMap<>();

    private JPanel centerPane;
    private CardLayout cardLayoutCenter;

    private final Consumer<LoginResponseDto> onOpenPdvScreen;

    public TelaInicialView(MainApplication mainApplication, LoginResponseDto funcionarioLogado,
            Set<String> botoesDesabilitados, Consumer<LoginResponseDto> onOpenPdvScreen) {
        if (mainApplication == null || funcionarioLogado == null) {
            throw new IllegalArgumentException("MainApplication e FuncionarioLogado não podem ser nulos.");
        }
        this.mainApplication = mainApplication;
        this.funcionarioLogado = funcionarioLogado;
        this.botoesDesabilitados = botoesDesabilitados != null ? botoesDesabilitados : new HashSet<>();
        this.onOpenPdvScreen = onOpenPdvScreen;

        initializeViewData();
        setupUI();
    }

    /**
     * Método para inicializar os botões com seus respectivos nomes e icones 
     */
    private void initializeViewData() {
        // Ícones e Botões
        botoesConfig.put("PDV", "/images/carinho.png");
        botoesConfig.put("Estoque", "/images/estoque.png");
        botoesConfig.put("Relatorio", "/images/grafico.png");
        botoesConfig.put("Funcionarios", "/images/funcionarios.png");
        botoesConfig.put("Ajuda", "/images/ajuda.png");
        botoesConfig.put("Sair", "/images/sair.png");
    }

    /**
     * 
     */
    private void setupUI() {
        setLayout(new BorderLayout());

        // Adiciona Top Bar e Sidebar
        add(new HeaderBar(funcionarioLogado), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);

        // Painel Central usando CardLayout
        cardLayoutCenter = new CardLayout();
        centerPane = new JPanel(cardLayoutCenter);

        // Painel padrão de Boas-Vindas
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("Bem-vindo ao Mercadinho Providence, " + funcionarioLogado.getName() + "!");
        welcomeLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +6");
        welcomePanel.add(welcomeLabel);

        centerPane.add(welcomePanel, "HOME");
        add(centerPane, BorderLayout.CENTER);

        configurarAtalhosDeTeclado();
    }

    /**
     * Barra Lateral com Logo no Topo (Borda a Borda) e Conteúdo dos Botões
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout()); // Alterado para BorderLayout para facilitar o topo
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.putClientProperty(FlatClientProperties.STYLE, "background: #f27a1f  ");

        // --- 1. Cabeçalho da Sidebar (Logo ocupando 100% da largura/topo) ---
        JLabel logoLabel = new LogoLabel(ImagemUtil.loadImage(getClass(),"/images/logoMercado.png", 240, 160));
        sidebar.add(logoLabel, BorderLayout.NORTH);

        // --- 2. Painel dos Botões do Menu ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        // Adiciona o espaçamento interno (padding) APENAS para os botões: 20px no topo,
        // 15px nas laterais e fundo
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 15));
        menuPanel.setOpaque(false);

        botoesConfig.forEach((texto, caminhoIcone) -> {
            if (!texto.equalsIgnoreCase("Sair")) {
                SideBarButton btn = new SideBarButton(texto, ImagemUtil.loadImage(getClass(),caminhoIcone, 22, 22));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                if (botoesDesabilitados.contains(texto)) {
                    btn.setEnabled(false);
                }
                handleButtonClick(texto, btn);
                botoesInstanciados.put(texto, btn);
                menuPanel.add(btn);

                // Espaçamento entre os botões (16px)
                menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            }
        });

        // Empurra o botão Sair para o final da tela
        menuPanel.add(Box.createVerticalGlue());

        JButton btnSair = new DangerSideBarButton("Sair", ImagemUtil.loadImage(getClass(),botoesConfig.get("Sair"), 22, 22));
        btnSair.setAlignmentX(Component.CENTER_ALIGNMENT);

        botoesInstanciados.put("Sair", btnSair);

        handleButtonClick("Sair", btnSair);
        menuPanel.add(btnSair);

        sidebar.add(menuPanel, BorderLayout.CENTER);

        return sidebar;
    }

    private void handleButtonClick(String texto, JButton btn) {
        if (texto.equalsIgnoreCase("PDV")) {
            btn.addActionListener(e -> {
                if (onOpenPdvScreen != null) {
                    onOpenPdvScreen.accept(funcionarioLogado);
                }
            });
            return;
        }

        if (texto.equalsIgnoreCase("Sair")) {
            btn.addActionListener(e -> System.out.println("Ação de Sair disparada."));
            return;
        }

        btn.addActionListener(e -> carregarTela(texto));
    }

    private void carregarTela(String nomeTela) {
        Supplier<JPanel> viewSupplier = telasConfig.get(nomeTela);
        if (viewSupplier != null) {
            JPanel novaTela = viewSupplier.get();
            centerPane.add(novaTela, nomeTela);
            cardLayoutCenter.show(centerPane, nomeTela);
        } else {
            System.out.println("DEBUG: Nenhuma tela mapeada para: " + nomeTela);
        }
    }

    private void configurarAtalhosDeTeclado() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "PDV_ACTION");
        actionMap.put("PDV_ACTION", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (onOpenPdvScreen != null) {
                    onOpenPdvScreen.accept(funcionarioLogado);
                }
            }
        });
    }

    // =========================================================================
    // MÉTODO MAIN
    // =========================================================================
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mercadinho Providence - Tela Inicial");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Tela Cheia / Maximizado
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setMinimumSize(new Dimension(1024, 600));

            LoginResponseDto mockUser = new LoginResponseDto();
            mockUser.setName("Carlos Silva");

            TelaInicialView view = new TelaInicialView(
                    new MainApplication(),
                    mockUser,
                    Set.of("Ajuda"),
                    user -> JOptionPane.showMessageDialog(frame, "Abrindo PDV para: " + user.getName()));

            frame.setContentPane(view);
            frame.setVisible(true);
        });
    }
}