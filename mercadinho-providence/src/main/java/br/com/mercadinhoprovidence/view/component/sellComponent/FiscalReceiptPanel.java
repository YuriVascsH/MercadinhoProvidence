package br.com.mercadinhoprovidence.view.component.sellComponent;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;

public class FiscalReceiptPanel extends JPanel {

    private static final Font FONT_CUPOM = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_RECEIPT_BOLD = new Font("Monospaced", Font.BOLD, 12);

    private int currentGridRow = 0;
    private JLabel titleCumpoLabel;
    private JPanel receiptItemsBox;

    public FiscalReceiptPanel() {
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        titleCumpoLabel = new JLabel("CUPOM FISCAL");
        titleCumpoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleCumpoLabel.setForeground(new Color(230, 81, 0));
        titleCumpoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleCumpoLabel, BorderLayout.NORTH);

        receiptItemsBox = new JPanel(new GridBagLayout());
        receiptItemsBox.setBackground(Color.WHITE);

        addReceiptHeader();
        addSeparatorLine();

        JScrollPane cupomScroll = new JScrollPane(receiptItemsBox);
        cupomScroll.setBorder(null);
        cupomScroll.getViewport().setBackground(Color.WHITE);

        add(cupomScroll, BorderLayout.CENTER);

    }

    /**
     * Adiciona os títulos das respectivas colunas. 
     */
    private void addReceiptHeader() {
        Color headerColor = new Color(110, 110, 110);

        JLabel lblQty = new JLabel("QTD", SwingConstants.CENTER);
        lblQty.setFont(FONT_RECEIPT_BOLD);
        lblQty.setForeground(headerColor);

        JLabel lblCode = new JLabel("CÓDIGO", SwingConstants.CENTER);
        lblCode.setFont(FONT_RECEIPT_BOLD);
        lblCode.setForeground(headerColor);

        JLabel lblDesc = new JLabel("DESCRIÇÃO", SwingConstants.CENTER);
        lblDesc.setFont(FONT_RECEIPT_BOLD);
        lblDesc.setForeground(headerColor);

        JLabel lblUnit = new JLabel("UNIT.(R$)", SwingConstants.CENTER);
        lblUnit.setFont(FONT_RECEIPT_BOLD);
        lblUnit.setForeground(headerColor);

        JLabel lblTotal = new JLabel("TOTAL(R$)", SwingConstants.RIGHT);
        lblTotal.setFont(FONT_RECEIPT_BOLD);
        lblTotal.setForeground(headerColor);

        // Adiciona a linha do cabeçalho incrementando o contador de linhas
        addRowToReceiptGrid(lblQty, lblCode, lblDesc, lblUnit, lblTotal, currentGridRow++);
    }

    /**
     * Adiciona uma linha de 5 colunas dentro do mesmo GridBagLayout.
     */
    private void addRowToReceiptGrid(JComponent cQtd, JComponent cCod, JComponent cDesc, JComponent cUnit,
            JComponent cTotal, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = gridy;
        gbc.insets = new Insets(2, 4, 2, 4);

        // Coluna 1: QTD (5%)
        gbc.gridx = 0;
        gbc.weightx = 0.05;
        receiptItemsBox.add(cQtd, gbc);

        // Coluna 2: CÓDIGO (20%)
        gbc.gridx = 1;
        gbc.weightx = 0.20;
        receiptItemsBox.add(cCod, gbc);

        // Coluna 3: DESCRIÇÃO (45%)
        gbc.gridx = 2;
        gbc.weightx = 0.45;
        receiptItemsBox.add(cDesc, gbc);

        // Coluna 4: UNIT (15%)
        gbc.gridx = 3;
        gbc.weightx = 0.15;
        receiptItemsBox.add(cUnit, gbc);

        // Coluna 5: TOTAL (15%)
        gbc.gridx = 4;
        gbc.weightx = 0.15;
        receiptItemsBox.add(cTotal, gbc);
    }

    /**
     * Adiciona uma linha divisória horizontal no cupom fiscal.
     */
    private void addSeparatorLine() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(new Color(210, 210, 210));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = currentGridRow++;
        gbc.gridwidth = 5; // Ocupa todas as 5 colunas do grid
        gbc.insets = new Insets(5, 0, 5, 0);

        receiptItemsBox.add(separator, gbc);
    }

    /**
     * Adiciona uma nova linha de produto ao cupom fiscal durante a execução.
     * Instancia os labels estilizados para cada coluna, adiciona ao grid
     * e revalida a interface gráfica.
     *
     * @param qtd         Quantidade do produto (ex: "2 UN", "0,500 KG")
     * @param code        Código do produto ou código de barras
     * @param description Descrição/nome do produto
     * @param unitPrice   Valor unitário formatado
     * @param totalPrice  Valor total do item formatado
     */
    public void adicionarItemAoCupom(String qtd, String code, String description, String unitPrice, String totalPrice) {
        JLabel lblQtd = new JLabel(qtd, SwingConstants.CENTER);
        lblQtd.setFont(FONT_CUPOM);

        JLabel lblCode = new JLabel(code, SwingConstants.CENTER);
        lblCode.setFont(FONT_CUPOM);
        lblCode.setForeground(Color.DARK_GRAY);

        JLabel lblDescription = new JLabel(description, SwingConstants.LEFT);
        lblDescription.setFont(FONT_CUPOM);

        JLabel lblUnitPrice = new JLabel(unitPrice, SwingConstants.CENTER);
        lblUnitPrice.setFont(FONT_CUPOM);

        JLabel lblTotalPrice = new JLabel(totalPrice, SwingConstants.RIGHT);
        lblTotalPrice.setFont(FONT_RECEIPT_BOLD);

        // Adiciona a nova linha de produto incrementando o gridy
        addRowToReceiptGrid(lblQtd, lblCode, lblDescription, lblUnitPrice, lblTotalPrice, currentGridRow++);

        // Atualiza a mola empurradora vertical no final para segurar o topo
        updateVerticalGlue();

        receiptItemsBox.revalidate();
        receiptItemsBox.repaint();
    }

    /**
     * Atualiza a mola empurradora vertical (espaçador) no final do grid de itens.
     */
    private void updateVerticalGlue() {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = currentGridRow + 1000; // Garante que fica ao final de tudo
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;

        receiptItemsBox.add(Box.createGlue(), gbc);
    }
}
