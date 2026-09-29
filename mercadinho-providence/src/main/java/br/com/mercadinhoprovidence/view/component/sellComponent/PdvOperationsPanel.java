package br.com.mercadinhoprovidence.view.component.sellComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Painel de operações do PDV (Lado Direito).
 * Contém os cards de informação do produto atual, quantidade, valor unitário,
 * total da compra e os campos de entrada (inputs) de dados.
 */
public class PdvOperationsPanel extends JPanel {

    private JLabel quantityValueLabel;
    private JLabel unitPriceValueLabel;
    private JLabel productNameLabel;
    private JLabel unitPriceDetailLabel;
    private JLabel currentItemTotalLabel;
    private JLabel purchaseTotalLabel;

    private JTextField quantityTextField;
    private JTextField productCodeTextField;

    public PdvOperationsPanel() {
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // 1. Box Quantidade e Valor Unitário
        add(createQuantityAndUnitPriceBox());
        add(Box.createVerticalStrut(10));

        // 2. Box Nome do Produto Atual
        add(createCurrentProductBox());
        add(Box.createVerticalStrut(10));

        // 3. Card Total da Compra
        add(createPurchaseTotalBox());
        add(Box.createVerticalStrut(10));

        // 4. Box de Entrada (Campos de Texto)
        add(createInputFieldsBox());
        add(Box.createVerticalGlue());
    }

    /**
     * Cria o card de quantidade e valor unitário do item atual.
     */
    private JPanel createQuantityAndUnitPriceBox() {
        JPanel container = new JPanel(new GridLayout(1, 2, 10, 0));
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Card Quantidade
        JPanel quantityCard = createBaseCard("QUANTIDADE");
        quantityValueLabel = new JLabel("0", SwingConstants.RIGHT);
        quantityValueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        quantityValueLabel.setForeground(new Color(13, 71, 161));
        quantityCard.add(quantityValueLabel, BorderLayout.SOUTH);
        container.add(quantityCard);

        // Card Valor Unitário
        JPanel unitPriceCard = createBaseCard("VALOR UNITÁRIO");
        unitPriceValueLabel = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        unitPriceValueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        unitPriceValueLabel.setForeground(new Color(13, 71, 161));
        unitPriceCard.add(unitPriceValueLabel, BorderLayout.SOUTH);
        container.add(unitPriceCard);

        return container;
    }

    /**
     * Cria o card que exibe o nome e os detalhes do produto atualmente selecionado.
     */
    private JPanel createCurrentProductBox() {
        JPanel productCard = createBaseCard("");
        productCard.setPreferredSize(new Dimension(0, 90));
        productCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        productNameLabel = new JLabel("NENHUM PRODUTO SELECIONADO", SwingConstants.CENTER);
        productNameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        productNameLabel.setForeground(new Color(33, 33, 33));

        JPanel subInfoBox = new JPanel(new BorderLayout());
        subInfoBox.setOpaque(false);

        unitPriceDetailLabel = new JLabel("0X", SwingConstants.LEFT);
        unitPriceDetailLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        unitPriceDetailLabel.setForeground(Color.GRAY);

        currentItemTotalLabel = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        currentItemTotalLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        currentItemTotalLabel.setForeground(Color.GRAY);

        subInfoBox.add(unitPriceDetailLabel, BorderLayout.WEST);
        subInfoBox.add(currentItemTotalLabel, BorderLayout.EAST);

        productCard.add(productNameLabel, BorderLayout.CENTER);
        productCard.add(subInfoBox, BorderLayout.SOUTH);

        return productCard;
    }

    /**
     * Cria o card destacado para exibição do valor total da compra.
     */
    private JPanel createPurchaseTotalBox() {
        JPanel totalBox = new JPanel(new BorderLayout());
        totalBox.setBackground(new Color(230, 81, 0));
        totalBox.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        totalBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        JLabel titleLabel = new JLabel("TOTAL DA COMPRA");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        purchaseTotalLabel = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        purchaseTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        purchaseTotalLabel.setForeground(Color.WHITE);

        totalBox.add(titleLabel, BorderLayout.NORTH);
        totalBox.add(purchaseTotalLabel, BorderLayout.SOUTH);

        return totalBox;
    }

    /**
     * Cria o formulário de entrada com os campos de Quantidade e Código de Barras.
     */
    private JPanel createInputFieldsBox() {
        JPanel inputCard = createBaseCard("");
        inputCard.setLayout(new GridBagLayout());
        inputCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);

        JLabel lblQuantityInput = new JLabel("QUANTIDADE");
        lblQuantityInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblQuantityInput.setForeground(Color.GRAY);

        quantityTextField = new JTextField("1");
        quantityTextField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblCodeInput = new JLabel("CÓDIGO DE BARRAS / PRODUTO");
        lblCodeInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblCodeInput.setForeground(Color.GRAY);

        productCodeTextField = new JTextField();
        productCodeTextField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Posições no Grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        inputCard.add(lblQuantityInput, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        inputCard.add(lblCodeInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputCard.add(quantityTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputCard.add(productCodeTextField, gbc);

        return inputCard;
    }

    /**
     * Cria a estrutura base cinza para os cards de informação.
     *
     * @param title Título do card (caso exista)
     * @return JPanel formatado
     */
    private JPanel createBaseCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(236, 239, 241));
        card.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        if (title != null && !title.trim().isEmpty()) {
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblTitle.setForeground(new Color(97, 97, 97));
            card.add(lblTitle, BorderLayout.NORTH);
        }

        return card;
    }

    // =========================================================================
    // MÉTODOS PÚBLICOS DE ATUALIZAÇÃO E INTERAÇÃO (GETTERS / SETTERS / LISTENERS)
    // =========================================================================

    /**
     * Atualiza os dados do produto ativo na interface.
     */
    public void updateCurrentProductDisplay(String name, String qtyDisplay, String unitPriceDisplay, String totalDisplay) {
        this.productNameLabel.setText(name);
        this.quantityValueLabel.setText(qtyDisplay);
        this.unitPriceValueLabel.setText(unitPriceDisplay);
        this.unitPriceDetailLabel.setText(qtyDisplay + " X " + unitPriceDisplay);
        this.currentItemTotalLabel.setText(totalDisplay);
    }

    /**
     * Atualiza o valor do total geral da compra.
     */
    public void updatePurchaseTotal(String total) {
        this.purchaseTotalLabel.setText(total);
    }

    /**
     * Define a ação disparada ao pressionar Enter no campo do código de produto.
     */
    public void addProductCodeActionListener(ActionListener listener) {
        this.productCodeTextField.addActionListener(listener);
    }

    public JTextField getQuantityTextField() {
        return quantityTextField;
    }

    public JTextField getProductCodeTextField() {
        return productCodeTextField;
    }
}