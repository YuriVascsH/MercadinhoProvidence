package br.com.mercadinhoprovidence.view.component.sellComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FooterBox extends JPanel {

    private JLabel titleLabel;
    private JLabel shortcutsLabel;
    private JButton btnExit;

    public FooterBox(ActionListener actionListener) {
        setupLayout();
        buildFooter(actionListener);
    }

    /**
     * Métoodo para aplicar os estilos e definir o layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(74, 74, 74));
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        titleLabel = new JLabel("MERCADINHO PROVIDENCE");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        shortcutsLabel = new JLabel("Atalhos: [F3] Modo | [F4] Cancelar | [F10] Finalizar", SwingConstants.CENTER);
        shortcutsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        shortcutsLabel.setForeground(new Color(255, 204, 128));

        btnExit = new JButton("Sair da Venda (ESC)");
        btnExit.putClientProperty("FlatLaf.style", "background: #dc3545; foreground: #ffffff; font: bold;");
        add(titleLabel, BorderLayout.WEST);
        add(shortcutsLabel, BorderLayout.CENTER);
        add(btnExit, BorderLayout.EAST);
    }

    /**
     * Método para aplicar a lógica do footer
     */
    private void buildFooter(ActionListener actionListener) {
        if (actionListener != null)
            btnExit.addActionListener(actionListener);

    }

}
