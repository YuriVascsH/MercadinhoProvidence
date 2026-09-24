package br.com.mercadinhoprovidence.view;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import br.com.mercadinhoprovidence.config.ScreenNavigator;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;

public class TelaPdvPanel extends JPanel {

    private final ScreenNavigator navigator;
    private LoginResponseDto funcionarioLogado;

    // Fontes do Cupom Fiscal (Estilo Impressora Térmica)
    private static final Font FONT_CUPOM = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_CUPOM_BOLD = new Font("Monospaced", Font.BOLD, 12);

    // Contêiner com GridBagLayout único para todo o cupom (Cabeçalho + Itens)
    private JPanel cupomFiscalItemsBox;
    private int linhaAtualCupom = 0; // Controle dinâmico de linhas (gridy)

    // Modos
    private boolean modoRemocaoAtivo = false;

    // Componentes Visuais do Painel Direito
    private JLabel statusModoLabel;
    private JLabel quantidadeValorLabel;
    private JLabel precoUnitarioLabel;
    private JLabel nomeProdutoLabel;
    private JLabel unidadePrecoLabel;
    private JLabel totalItemAtualLabel;
    private JLabel totalCompraLabel;

    private JTextField tfCodigoProduto;
    private JTextField tfQuantidade;

    public TelaPdvPanel(ScreenNavigator navigator) {
        this.navigator = navigator;

        setupUI();
        setupKeyBindings();
        // Dados demonstrativos de teste (sem o "R$" nos valores)
        adicionarItemAoCupom("2 UN", "7891000123456", "LEITE INTEGRAL 1L", "4,50", "9,00");
        adicionarItemAoCupom("1 UN", "7891000654321", "CAFÉ TORRADO EXTRA FORTE 500G", "14,90", "14,90");
        adicionarItemAoCupom("3 UN", "7891000987654", "BISCOITO RECHEADO CHOCOLATE 130G", "3,20", "9,60");

        // Produtos vendidos por peso (KG)
        adicionarItemAoCupom("0,350 KG", "00012", "QUEIJO MUÇARELA FATIADO", "52,00", "18,20");
        adicionarItemAoCupom("1,245 KG", "00088", "ALCATRA BOVINA FRESCA", "45,90", "57,14");
        adicionarItemAoCupom("0,820 KG", "00045", "BANANA PRATA", "6,50", "5,33");

    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // 1. Status Modo (Barra Superior)
        statusModoLabel = new JLabel("Modo: Adição (F3 para Remoção)", SwingConstants.CENTER);
        statusModoLabel.setOpaque(true);
        statusModoLabel.setBackground(new Color(76, 175, 80));
        statusModoLabel.setForeground(Color.WHITE);
        statusModoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusModoLabel.setPreferredSize(new Dimension(0, 32));
        add(statusModoLabel, BorderLayout.NORTH);

        // 2. Painel Central
        JPanel centroBox = new JPanel(new GridBagLayout());
        centroBox.setOpaque(false);
        centroBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- Esquerda: Recibo do Cupom ---
        gbc.gridx = 0;
        gbc.weightx = 0.58;
        gbc.insets = new Insets(0, 0, 0, 5);
        centroBox.add(criarParteEsquerdaCupom(), gbc);

        // --- Direita: Cards de Informações ---
        gbc.gridx = 1;
        gbc.weightx = 0.42;
        gbc.insets = new Insets(0, 5, 0, 0);
        centroBox.add(criarParteDireitaOperacoes(), gbc);

        add(centroBox, BorderLayout.CENTER);

        // 3. Rodapé
        add(criarFooterBox(), BorderLayout.SOUTH);
    }

    // =========================================================================
    // ESTRUTURA VISUAL DO CUPOM FISCAL (LAYOUT COM GRIDBAG ÚNICO)
    // =========================================================================
    private JPanel criarParteEsquerdaCupom() {
        JPanel parteEsquerdaBox = new JPanel(new BorderLayout());
        parteEsquerdaBox.setBackground(Color.WHITE);
        parteEsquerdaBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Título Superior
        JLabel tituloCupomLabel = new JLabel("CUPOM FISCAL");
        tituloCupomLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloCupomLabel.setForeground(new Color(230, 81, 0));
        tituloCupomLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        parteEsquerdaBox.add(tituloCupomLabel, BorderLayout.NORTH);

        // Painel Único que conterá todas as linhas (Cabeçalho + Divisória + Itens)
        cupomFiscalItemsBox = new JPanel(new GridBagLayout());
        cupomFiscalItemsBox.setBackground(Color.WHITE);

        // Adiciona Cabeçalho na Linha 0 e Divisória na Linha 1
        adicionarCabecalhoCupom();
        adicionarLinhaDivisoria();

        // ScrollPane
        JScrollPane cupomScroll = new JScrollPane(cupomFiscalItemsBox);
        cupomScroll.setBorder(null);
        cupomScroll.getViewport().setBackground(Color.WHITE);

        parteEsquerdaBox.add(cupomScroll, BorderLayout.CENTER);

        return parteEsquerdaBox;
    }

    /**
     * Adiciona uma linha de 5 colunas dentro do mesmo GridBagLayout.
     */
    private void adicionarLinhaNoGridCupom(JComponent cQtd, JComponent cCod, JComponent cDesc, JComponent cUnit,
            JComponent cTotal, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = gridy;
        gbc.insets = new Insets(2, 4, 2, 4);

        // Coluna 1: QTD (5%)
        gbc.gridx = 0;
        gbc.weightx = 0.05;
        cupomFiscalItemsBox.add(cQtd, gbc);

        // Coluna 2: CÓDIGO (20%)
        gbc.gridx = 1;
        gbc.weightx = 0.20;
        cupomFiscalItemsBox.add(cCod, gbc);

        // Coluna 3: DESCRIÇÃO (45%)
        gbc.gridx = 2;
        gbc.weightx = 0.45;
        cupomFiscalItemsBox.add(cDesc, gbc);

        // Coluna 4: UNIT (15%)
        gbc.gridx = 3;
        gbc.weightx = 0.15;
        cupomFiscalItemsBox.add(cUnit, gbc);

        // Coluna 5: TOTAL (15%)
        gbc.gridx = 4;
        gbc.weightx = 0.15;
        cupomFiscalItemsBox.add(cTotal, gbc);
    }

    private void adicionarCabecalhoCupom() {
        Color colorHeader = new Color(110, 110, 110);

        JLabel lblQtd = new JLabel("QTD", SwingConstants.CENTER);
        lblQtd.setFont(FONT_CUPOM_BOLD);
        lblQtd.setForeground(colorHeader);

        JLabel lblCodigo = new JLabel("CÓDIGO", SwingConstants.CENTER);
        lblCodigo.setFont(FONT_CUPOM_BOLD);
        lblCodigo.setForeground(colorHeader);

        JLabel lblDesc = new JLabel("DESCRIÇÃO", SwingConstants.CENTER);
        lblDesc.setFont(FONT_CUPOM_BOLD);
        lblDesc.setForeground(colorHeader);

        JLabel lblUnit = new JLabel("UNIT.(R$)", SwingConstants.CENTER);
        lblUnit.setFont(FONT_CUPOM_BOLD);
        lblUnit.setForeground(colorHeader);

        JLabel lblTotal = new JLabel("TOTAL(R$)", SwingConstants.RIGHT);
        lblTotal.setFont(FONT_CUPOM_BOLD);
        lblTotal.setForeground(colorHeader);

        adicionarLinhaNoGridCupom(lblQtd, lblCodigo, lblDesc, lblUnit, lblTotal, linhaAtualCupom++);
    }

    private void adicionarLinhaDivisoria() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(new Color(210, 210, 210));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = linhaAtualCupom++;
        gbc.gridwidth = 5; // Ocupa todas as 5 colunas
        gbc.insets = new Insets(5, 0, 5, 0);

        cupomFiscalItemsBox.add(separator, gbc);
    }

    public void adicionarItemAoCupom(String qtd, String codigo, String descricao, String unitario, String total) {
        JLabel lblQtd = new JLabel(qtd, SwingConstants.CENTER);
        lblQtd.setFont(FONT_CUPOM);

        JLabel lblCodigo = new JLabel(codigo, SwingConstants.CENTER);
        lblCodigo.setFont(FONT_CUPOM);
        lblCodigo.setForeground(Color.DARK_GRAY);

        JLabel lblDesc = new JLabel(descricao, SwingConstants.LEFT);
        lblDesc.setFont(FONT_CUPOM);

        JLabel lblUnit = new JLabel(unitario, SwingConstants.CENTER);
        lblUnit.setFont(FONT_CUPOM);

        JLabel lblTotal = new JLabel(total, SwingConstants.RIGHT);
        lblTotal.setFont(FONT_CUPOM_BOLD);

        // Adiciona a nova linha de produto incrementando o gridy
        adicionarLinhaNoGridCupom(lblQtd, lblCodigo, lblDesc, lblUnit, lblTotal, linhaAtualCupom++);

        // Atualiza a mola empurradora vertical no final para segurar o topo
        atualizarMolaEmpurradoraVertical();

        cupomFiscalItemsBox.revalidate();
        cupomFiscalItemsBox.repaint();
    }

    private void atualizarMolaEmpurradoraVertical() {
        // Remove mola antiga se existir e insere na última linha disponível
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = linhaAtualCupom + 1000; // Garante que fica ao final de tudo
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;

        cupomFiscalItemsBox.add(Box.createGlue(), gbc);
    }

    // =========================================================================
    // PARTE DIREITA: CARDS DE INFORMAÇÃO E LEITURA DE PRODUTOS
    // =========================================================================
    private JPanel criarParteDireitaOperacoes() {
        JPanel parteDireitaBox = new JPanel();
        parteDireitaBox.setLayout(new BoxLayout(parteDireitaBox, BoxLayout.Y_AXIS));
        parteDireitaBox.setOpaque(false);

        // 1. Box Quantidade e Valor Unitário
        JPanel linhaQtdeValor = new JPanel(new GridLayout(1, 2, 10, 0));
        linhaQtdeValor.setOpaque(false);
        linhaQtdeValor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel quantidadeBox = criarCardBase("QUANTIDADE");
        quantidadeValorLabel = new JLabel("0", SwingConstants.RIGHT);
        quantidadeValorLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        quantidadeValorLabel.setForeground(new Color(13, 71, 161));
        quantidadeBox.add(quantidadeValorLabel, BorderLayout.SOUTH);
        linhaQtdeValor.add(quantidadeBox);

        JPanel valorUnitarioBox = criarCardBase("VALOR UNITÁRIO");
        precoUnitarioLabel = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        precoUnitarioLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        precoUnitarioLabel.setForeground(new Color(13, 71, 161));
        valorUnitarioBox.add(precoUnitarioLabel, BorderLayout.SOUTH);
        linhaQtdeValor.add(valorUnitarioBox);

        parteDireitaBox.add(linhaQtdeValor);
        parteDireitaBox.add(Box.createVerticalStrut(10));

        // 2. Box Nome do Produto Atual
        JPanel nomeValorBox = criarCardBase("");
        nomeValorBox.setPreferredSize(new Dimension(0, 90));
        nomeValorBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        nomeProdutoLabel = new JLabel("NENHUM PRODUTO SELECIONADO", SwingConstants.CENTER);
        nomeProdutoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nomeProdutoLabel.setForeground(new Color(33, 33, 33));

        JPanel subInfoBox = new JPanel(new BorderLayout());
        subInfoBox.setOpaque(false);
        unidadePrecoLabel = new JLabel("0X", SwingConstants.LEFT);
        unidadePrecoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        unidadePrecoLabel.setForeground(Color.GRAY);

        totalItemAtualLabel = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        totalItemAtualLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        totalItemAtualLabel.setForeground(Color.GRAY);

        subInfoBox.add(unidadePrecoLabel, BorderLayout.WEST);
        subInfoBox.add(totalItemAtualLabel, BorderLayout.EAST);

        nomeValorBox.add(nomeProdutoLabel, BorderLayout.CENTER);
        nomeValorBox.add(subInfoBox, BorderLayout.SOUTH);

        parteDireitaBox.add(nomeValorBox);
        parteDireitaBox.add(Box.createVerticalStrut(10));

        // 3. Card Total da Compra
        JPanel totalCompraBox = new JPanel(new BorderLayout());
        totalCompraBox.setBackground(new Color(230, 81, 0));
        totalCompraBox.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        totalCompraBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        JLabel valorFinalLabel = new JLabel("TOTAL DA COMPRA");
        valorFinalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        valorFinalLabel.setForeground(Color.WHITE);

        totalCompraLabel = new JLabel("R$ 33,50", SwingConstants.RIGHT);
        totalCompraLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        totalCompraLabel.setForeground(Color.WHITE);

        totalCompraBox.add(valorFinalLabel, BorderLayout.NORTH);
        totalCompraBox.add(totalCompraLabel, BorderLayout.SOUTH);

        parteDireitaBox.add(totalCompraBox);
        parteDireitaBox.add(Box.createVerticalStrut(10));

        // 4. Box de Entrada (Campos)
        JPanel inputCodigoBarrasBox = criarCardBase("");
        inputCodigoBarrasBox.setLayout(new GridBagLayout());
        inputCodigoBarrasBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);

        JLabel lblQtdInput = new JLabel("QUANTIDADE");
        lblQtdInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblQtdInput.setForeground(Color.GRAY);

        tfQuantidade = new JTextField("1");
        tfQuantidade.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblCodigoInput = new JLabel("CÓDIGO DE BARRAS / PRODUTO");
        lblCodigoInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblCodigoInput.setForeground(Color.GRAY);

        tfCodigoProduto = new JTextField();
        tfCodigoProduto.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tfCodigoProduto.addActionListener(e -> processarLeituraProduto());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        inputCodigoBarrasBox.add(lblQtdInput, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        inputCodigoBarrasBox.add(lblCodigoInput, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputCodigoBarrasBox.add(tfQuantidade, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputCodigoBarrasBox.add(tfCodigoProduto, gbc);

        parteDireitaBox.add(inputCodigoBarrasBox);
        parteDireitaBox.add(Box.createVerticalGlue());

        return parteDireitaBox;
    }

    private JPanel criarCardBase(String titulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(236, 239, 241));
        card.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        if (!titulo.isEmpty()) {
            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblTitulo.setForeground(new Color(97, 97, 97));
            card.add(lblTitulo, BorderLayout.NORTH);
        }

        return card;
    }

    // =========================================================================
    // RODAPÉ DA TELA
    // =========================================================================
    private JPanel criarFooterBox() {
        JPanel footerBox = new JPanel(new BorderLayout());
        footerBox.setBackground(new Color(74, 74, 74));
        footerBox.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel empresaFooterLabel = new JLabel("MERCADINHO PROVIDENCE");
        empresaFooterLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        empresaFooterLabel.setForeground(Color.WHITE);

        JLabel atalhosLabel = new JLabel("Atalhos: [F3] Modo | [F4] Cancelar | [F10] Finalizar", SwingConstants.CENTER);
        atalhosLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        atalhosLabel.setForeground(new Color(255, 204, 128));

        JButton btnSair = new JButton("Sair da Venda (ESC)");
        btnSair.putClientProperty("FlatLaf.style", "background: #dc3545; foreground: #ffffff; bold: true;");
        btnSair.addActionListener(e -> confirmarESair());

        footerBox.add(empresaFooterLabel, BorderLayout.WEST);
        footerBox.add(atalhosLabel, BorderLayout.CENTER);
        footerBox.add(btnSair, BorderLayout.EAST);

        return footerBox;
    }

    // =========================================================================
    // LÓGICA DE TECLAS E AÇÕES
    // =========================================================================
    private void setupKeyBindings() {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "alternarModo");
        getActionMap().put("alternarModo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alternarModoRemocao();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "sair");
        getActionMap().put("sair", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarESair();
            }
        });
    }

    private void alternarModoRemocao() {
        modoRemocaoAtivo = !modoRemocaoAtivo;
        if (modoRemocaoAtivo) {
            statusModoLabel.setText("MODO: REMOÇÃO ATIVO (F3 para Adição)");
            statusModoLabel.setBackground(new Color(211, 47, 47));
        } else {
            statusModoLabel.setText("Modo: Adição (F3 para Remoção)");
            statusModoLabel.setBackground(new Color(76, 175, 80));
        }
    }

    private void processarLeituraProduto() {
        String codigo = tfCodigoProduto.getText().trim();
        String qtd = tfQuantidade.getText().trim();

        if (codigo.isEmpty())
            return;

        adicionarItemAoCupom(qtd, codigo, "PRODUTO TESTE " + codigo, "R$ 10,00",
                "R$ " + (Integer.parseInt(qtd) * 10) + ",00");

        nomeProdutoLabel.setText("PRODUTO TESTE " + codigo);
        quantidadeValorLabel.setText(qtd);
        precoUnitarioLabel.setText("R$ 10,00");
        unidadePrecoLabel.setText(qtd + "X");
        totalItemAtualLabel.setText("R$ " + (Integer.parseInt(qtd) * 10) + ",00");

        tfCodigoProduto.setText("");
        tfQuantidade.setText("1");
    }

    private void confirmarESair() {
        int opt = JOptionPane.showConfirmDialog(
                this,
                "Deseja sair da Tela de Venda?",
                "Confirmação de Saída",
                JOptionPane.YES_NO_OPTION);

        if (opt == JOptionPane.YES_OPTION && navigator != null) {
            navigator.home(this.funcionarioLogado);
        }
    }

    public void setFuncionarioLogado(LoginResponseDto funcionario) {
        this.funcionarioLogado = funcionario;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mercadinho Providence - PDV");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1150, 720);
            frame.setLocationRelativeTo(null);

            TelaPdvPanel pdvPanel = new TelaPdvPanel(null);

            frame.setContentPane(pdvPanel);
            frame.setVisible(true);
        });
    }
}