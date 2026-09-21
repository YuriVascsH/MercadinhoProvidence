package br.com.mercadinhoprovidence.view;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

/**
 * Card simples usado para exibir um indicador (título + valor colorido).
 * Extraído do método criarCardInfo(...) de TelaInicialView.
 */
public class CardInfo extends JPanel {

    public CardInfo(String titulo, String valor, String corHexValor) {
        super(new BorderLayout(5, 10));

        setBackground(Color.WHITE);
        putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #ffffff");
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(0, 2));
        topPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.putClientProperty(FlatClientProperties.STYLE, "font: +2; foreground: #000000");

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground: #6f6c6c");

        topPanel.add(lblTitulo, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.SOUTH);

        JLabel lblValor = new JLabel(valor);
        lblValor.putClientProperty(FlatClientProperties.STYLE, "font: bold +3; foreground: " + corHexValor);

        add(topPanel, BorderLayout.NORTH);
        add(lblValor, BorderLayout.CENTER);
    }
}