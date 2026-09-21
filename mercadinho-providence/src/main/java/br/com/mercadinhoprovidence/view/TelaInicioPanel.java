package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.MainApplication;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.view.component.CupomFiscalDialog;
import br.com.mercadinhoprovidence.view.component.HeaderBar;
import br.com.mercadinhoprovidence.view.component.SideBar;
import br.com.mercadinhoprovidence.view.component.UltimasVendasPanel;
import br.com.mercadinhoprovidence.view.panel.BoasVindasHeaderPanel;
import br.com.mercadinhoprovidence.view.panel.CardsResumoPanel;
import br.com.mercadinhoprovidence.view.panel.TelaFuncionariosPanel;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Tela inicial do sistema. Antes era TelaInicialView — agora só orquestra
 * os componentes (BoasVindasHeaderPanel, CardsResumoPanel, UltimasVendasPanel,
 * CupomFiscalDialog) em vez de montar tudo em um único método gigante.
 */
public class TelaInicioPanel extends JPanel {

    private static final String CHAVE_INICIO = "Inicio";

    private final MainApplication mainApplication;
    private final LoginResponseDto funcionarioLogado;

    private final Map<String, String> botoesConfig = new LinkedHashMap<>();
    private final Map<String, Supplier<JPanel>> telasConfig = new LinkedHashMap<>();
    private final Map<String, JPanel> telasInstanciadas = new HashMap<>();
    private final Set<String> botoesDesabilitados;
    private JPanel centerPane;
    private CardLayout cardLayoutCenter;

    private final Consumer<LoginResponseDto> onOpenPdvScreen;

    public TelaInicioPanel(MainApplication mainApplication, LoginResponseDto funcionarioLogado,
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

    private void initializeViewData() {
        botoesConfig.put(CHAVE_INICIO, "/images/home.png");
        botoesConfig.put("PDV", "/images/carinho.png");
        botoesConfig.put("Estoque", "/images/estoque.png");
        botoesConfig.put("Relatorio", "/images/grafico.png");
        botoesConfig.put("Funcionarios", "/images/funcionarios.png");
        botoesConfig.put("Ajuda", "/images/ajuda.png");
        botoesConfig.put("Sair", "/images/sair.png");

        telasConfig.put("Funcionarios", () -> new TelaFuncionariosPanel(funcionarioLogado));
    }

    private JPanel criarTelaPlaceholder(String mensagem) {
        JPanel placeholder = new JPanel(new GridBagLayout());
        placeholder.add(new JLabel(mensagem));
        return placeholder;
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        SideBar sideBar = new SideBar(botoesConfig, botoesDesabilitados);

        add(new HeaderBar(funcionarioLogado), BorderLayout.NORTH);
        add(sideBar, BorderLayout.WEST);

        sideBar.setOnButtonClick((texto, btn) -> {
            if (texto.equalsIgnoreCase("PDV")) {
                if (onOpenPdvScreen != null) {
                    onOpenPdvScreen.accept(funcionarioLogado);
                }
            } else if (texto.equalsIgnoreCase("Sair")) {
                System.out.println("Ação de Sair disparada.");
            } else {
                carregarTela(texto);
            }
        });

        cardLayoutCenter = new CardLayout();
        centerPane = new JPanel(cardLayoutCenter);
        

        JPanel welcomePanel = criarWelcomePanel();
        telasInstanciadas.put(CHAVE_INICIO, welcomePanel);
        centerPane.add(welcomePanel, CHAVE_INICIO);
        

        add(centerPane, BorderLayout.CENTER);
        cardLayoutCenter.show(centerPane, CHAVE_INICIO);

        configurarAtalhosDeTeclado();
    }

    private JPanel criarWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout(20, 20));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topContainer = new JPanel(new BorderLayout(0, 20));
        topContainer.add(new BoasVindasHeaderPanel(funcionarioLogado.getName()), BorderLayout.NORTH);
        topContainer.add(new CardsResumoPanel(), BorderLayout.SOUTH);

        JPanel bottomContainer = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomContainer.setOpaque(false);
        bottomContainer.add(new UltimasVendasPanel(this::abrirCupomFiscal));

        welcomePanel.add(topContainer, BorderLayout.NORTH);
        welcomePanel.add(bottomContainer, BorderLayout.CENTER);
        return welcomePanel;
    }

    private void abrirCupomFiscal(String codVenda, String total,
            List<br.com.mercadinhoprovidence.dto.venda.ItemVendaDto> itens) {
        String nomeOperador = funcionarioLogado != null ? funcionarioLogado.getName() : "OPERADOR PADRÃO";
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        new CupomFiscalDialog(owner, codVenda, total, nomeOperador, itens).setVisible(true);
    }

    private void carregarTela(String nomeTela) {
        if (CHAVE_INICIO.equalsIgnoreCase(nomeTela)) {
            cardLayoutCenter.show(centerPane, CHAVE_INICIO);
            return;
        }

        JPanel tela = telasInstanciadas.get(nomeTela);
        if (tela == null) {
            Supplier<JPanel> viewSupplier = telasConfig.get(nomeTela);
            if (viewSupplier == null) {
                System.out.println("DEBUG: Nenhuma tela mapeada para: " + nomeTela);
                return;
            }
            tela = viewSupplier.get();
            telasInstanciadas.put(nomeTela, tela);
            centerPane.add(tela, nomeTela);
        }

        cardLayoutCenter.show(centerPane, nomeTela);
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

    public static void main(String[] args) {
        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mercadinho Providence - Tela Inicial");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setMinimumSize(new Dimension(1024, 600));

            LoginResponseDto mockUser = new LoginResponseDto();
            mockUser.setName("Carlos Silva");

            TelaInicioPanel view = new TelaInicioPanel(
                    new MainApplication(),
                    mockUser,
                    Set.of("Ajuda"),
                    user -> JOptionPane.showMessageDialog(frame, "Abrindo PDV para: " + user.getName()));

            frame.setContentPane(view);
            frame.setVisible(true);
        });
    }
}