package br.com.mercadinhoprovidence.view;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import br.com.mercadinhoprovidence.balance.Weight;
import br.com.mercadinhoprovidence.config.ScreenNavigator;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.model.enums.Category;
import br.com.mercadinhoprovidence.view.component.sellComponent.FiscalReceiptPanel;
import br.com.mercadinhoprovidence.view.component.sellComponent.FooterBox;
import br.com.mercadinhoprovidence.view.component.sellComponent.PdvOperationsPanel;

public class TelaPdvPanel extends JPanel {

    private final ScreenNavigator navigator;
    private LoginResponseDto funcionarioLogado;

    private FiscalReceiptPanel fiscalReceiptPanel;
    private PdvOperationsPanel operationsPanel;
    // Modos
    private boolean modoRemocaoAtivo = false;

    // Componentes Visuais do Painel Direito
    private JLabel statusModoLabel;

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
        fiscalReceiptPanel = new FiscalReceiptPanel();
        centroBox.add(fiscalReceiptPanel, gbc);

        // --- Direita: Cards de Informações ---
        gbc.gridx = 1;
        gbc.weightx = 0.42;
        gbc.insets = new Insets(0, 5, 0, 0);
        operationsPanel = new PdvOperationsPanel();
        // operationsPanel.addProductCodeActionListener(e -> processarLeituraProduto());
        centroBox.add(operationsPanel, gbc);

        add(centroBox, BorderLayout.CENTER);

        // 3. Rodapé
        add(new FooterBox(e -> confirmarESair()), BorderLayout.SOUTH);
    }

    public void adicionarItemAoCupom(String qtd, String codigo, String descricao, String unitario, String total) {
        if (fiscalReceiptPanel != null) {
            fiscalReceiptPanel.adicionarItemAoCupom(qtd, codigo, descricao, unitario, total);
        }
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

    /***
     * Método responsável por realizar a leitura do produto.
     */
    private void processarLeituraProduto() {
        String code = operationsPanel.getProductCodeTextField().getText().trim();
        String qtd = operationsPanel.getQuantityTextField().getText().trim();

        if (code.isEmpty())
            return;

        ProdcutDto prodcutDto = sellController.findProduct(code);
        if (prodcutDto == null) {
            // optional de erro
        }
        BigDecimal quantidade;
        String qtdFormatada;

        if (prodcutDto.getCategory().getUnit().equalsIgnoreCase(Category.UNIDADE)) {

            try {
                quantidade = new BigDecimal(qtd.replace(",", "."));
                if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
                    quantidade = BigDecimal.ONE;
                }
            } catch (NumberFormatException e) {
                quantidade = BigDecimal.ONE;
            }

            qtdFormatada = String.format("%d UN", quantidade.intValue());

        } else {
            Weight pesoLido = balance.readWeight();

            if (pesoLido == null || pesoLido.getValor() <= 0) {
                // Option de erro!
                return;
            }
            quantidade = BigDecimal.valueOf(pesoLido.getValue());
            qtdFormatada = String.format(Locale.US, "%.3f KG", quantidade).replace('.', ',');

        }
        BigDecimal unitPrice = BigDecimal.valueOf(prodcutDto.getPrice());
        BigDecimal totalValueFloat = unitPrice.multiply(quantidade);

        String unitarioStr = String.format(Locale.US, "%.2f", unitPrice).replace(".", ",");
        String totalValueStr = String.format(Locale.US, "%.2f", totalValueFloat).replace(".", ",");

        operationsPanel.updateCurrentProductDisplay(
                productDto.getName(),
                qtdFormatada,
                "R$ " + unitarioStr,
                "R$ " + totalValueStr);

        adicionarItemAoCupom(
                qtdFormatada,
                prodcutDto.getCodigoVerificador(),
                prodcutDto.getName(),
                unitarioStr,
                totalValueStr);

        resetInputFields();

    }

    /**
     * Reseta os campos de entrada de texto para a próxima leitura.
     */
    private void resetInputFields() {
        operationsPanel.getProductCodeTextField().setText("");
        operationsPanel.getQuantityTextField().setText("1");
        operationsPanel.getProductCodeTextField().requestFocus();
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
