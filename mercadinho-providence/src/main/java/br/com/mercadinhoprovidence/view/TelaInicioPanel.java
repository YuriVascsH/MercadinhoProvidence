package br.com.mercadinhoprovidence.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import br.com.mercadinhoprovidence.config.ScreenNavigator;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.dto.venda.ItemVendaDto;
import br.com.mercadinhoprovidence.view.component.CupomFiscalDialog;
import br.com.mercadinhoprovidence.view.component.HeaderBar;
import br.com.mercadinhoprovidence.view.component.SideBar;
import br.com.mercadinhoprovidence.view.component.UltimasVendasPanel;
import br.com.mercadinhoprovidence.view.panel.BoasVindasHeaderPanel;
import br.com.mercadinhoprovidence.view.panel.CardsResumoPanel;
import br.com.mercadinhoprovidence.view.panel.TelaFuncionariosPanel;

public class TelaInicioPanel extends JPanel {

    private static final String CHAVE_INICIO = "Inicio";

    private final ScreenNavigator navigator;
    private LoginResponseDto funcionarioLogado;

    private final Map<String, String> botoesConfig = new LinkedHashMap<>();
    private final Map<String, Supplier<JPanel>> telasConfig = new LinkedHashMap<>();
    private final Map<String, JPanel> telasInstanciadas = new HashMap<>();
    private final Set<String> botoesDesabilitados;
    
    private JPanel centerPane;
    private CardLayout cardLayoutCenter;
    private HeaderBar headerBar;

    public TelaInicioPanel(ScreenNavigator navigator, Set<String> botoesDesabilitados) {
        if (navigator == null) {
            throw new IllegalArgumentException("ScreenNavigator não pode ser nulo.");
        }
        this.navigator = navigator;
        this.botoesDesabilitados = botoesDesabilitados != null ? botoesDesabilitados : new HashSet<>();

        initializeViewData();
        setupUI();
    }

    /**
     * Atualiza o funcionário logado na tela e recompõe a barra superior e o painel de boas-vindas.
     */
    public void setFuncionarioLogado(LoginResponseDto funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
        
        // Reinstancia/atualiza o mapa de telas com os dados do funcionário
        telasConfig.put("Funcionarios", () -> new TelaFuncionariosPanel(this.funcionarioLogado));
        
        // Atualiza a barra de cabeçalho
        if (headerBar != null) {
            remove(headerBar);
        }
        headerBar = new HeaderBar(this.funcionarioLogado);
        add(headerBar, BorderLayout.NORTH);

        // Recria a tela de boas-vindas para atualizar o nome no título
        JPanel welcomePanel = criarWelcomePanel();
        telasInstanciadas.put(CHAVE_INICIO, welcomePanel);
        centerPane.add(welcomePanel, CHAVE_INICIO);

        revalidate();
        repaint();
    }

    private void initializeViewData() {
        botoesConfig.put(CHAVE_INICIO, "/images/home.png");
        botoesConfig.put("PDV", "/images/carinho.png");
        botoesConfig.put("Estoque", "/images/estoque.png");
        botoesConfig.put("Relatorio", "/images/grafico.png");
        botoesConfig.put("Funcionarios", "/images/funcionarios.png");
        botoesConfig.put("Ajuda", "/images/ajuda.png");
        botoesConfig.put("Sair", "/images/sair.png");
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        SideBar sideBar = new SideBar(botoesConfig, botoesDesabilitados);
        add(sideBar, BorderLayout.WEST);

        sideBar.setOnButtonClick((texto, btn) -> {
            if (texto.equalsIgnoreCase("PDV")) {
                this.navigator.pdv(this.funcionarioLogado);
            } else if (texto.equalsIgnoreCase("Sair")) {
                this.funcionarioLogado = null;
                this.telasInstanciadas.clear();
                this.navigator.login();
            } else {
                carregarTela(texto);
            }
        });

        cardLayoutCenter = new CardLayout();
        centerPane = new JPanel(cardLayoutCenter);

        add(centerPane, BorderLayout.CENTER);

        configurarAtalhosDeTeclado();
    }

    private JPanel criarWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout(20, 20));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String nomeOp = (funcionarioLogado != null && funcionarioLogado.getName() != null)
                ? funcionarioLogado.getName()
                : "Operador";

        JPanel topContainer = new JPanel(new BorderLayout(0, 20));
        topContainer.add(new BoasVindasHeaderPanel(nomeOp), BorderLayout.NORTH);
        topContainer.add(new CardsResumoPanel(), BorderLayout.SOUTH);

        JPanel bottomContainer = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomContainer.setOpaque(false);
        bottomContainer.add(new UltimasVendasPanel(this::abrirCupomFiscal));

        welcomePanel.add(topContainer, BorderLayout.NORTH);
        welcomePanel.add(bottomContainer, BorderLayout.CENTER);
        return welcomePanel;
    }

    private void abrirCupomFiscal(String codVenda, String total, List<ItemVendaDto> itens) {
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
                if (funcionarioLogado != null) {
                    navigator.pdv(funcionarioLogado);
                }
            }
        });
    }
}