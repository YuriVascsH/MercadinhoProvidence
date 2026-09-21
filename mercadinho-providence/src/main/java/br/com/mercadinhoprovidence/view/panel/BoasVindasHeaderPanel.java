package br.com.mercadinhoprovidence.view.panel;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

/**
 * Cabeçalho de boas-vindas exibido no topo da tela inicial.
 * Extraído do bloco headerPanel de TelaInicialView.setupUI().
 */
public class BoasVindasHeaderPanel extends JPanel {

    public BoasVindasHeaderPanel(String nomeFuncionario) {
        super(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Bem-vindo ao Mercadinho Providence, " + nomeFuncionario + "!");
        welcomeLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +8");

        JLabel subtitleLabel = new JLabel("O que você deseja fazer hoje?");
        subtitleLabel.putClientProperty(FlatClientProperties.STYLE, "font: -1; foreground: $Label.disabledForeground");

        add(welcomeLabel, BorderLayout.NORTH);
        add(subtitleLabel, BorderLayout.SOUTH);
    }
}