package br.com.mercadinhoprovidence.view.component.global;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

import java.awt.*;


/**
 * 
 * Código responsável por criar a parte superior da tela de cada sessão.
 * 
 * HeaderSection
 */
public class HeaderSection extends JPanel {

    private JTextField txtSearch;
    private JButton buttonCreate;

    private static final Color COR_LARANJA_PRIMARY = new Color(255, 85, 0);

    public HeaderSection(String title, String subtitle, String txtButton, Runnable clickNew) {
        setupUI(title, subtitle, txtButton, clickNew);
    }

    /**
     * Método responável por realizar a montagem do painel
     * 
     * @param title informa para indentificar qual área está o usuário. 
     * @param subtitle informa o que o usuário pode fazer na tela.
     * @param txtButton texto para informar o que o botão faz
     * @param clickNew método recebido para a funcionalidade do botão.
     */
    private void setupUI(String title, String subtitle, String txtButton, Runnable clickNew) {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout((new BoxLayout(titlePanel, BoxLayout.Y_AXIS)));
        titlePanel.setOpaque(false);

        JLabel labelTitle = new JLabel(title);
        labelTitle.putClientProperty(FlatClientProperties.STYLE, "font: +10 bold; foreground: #FFFFFF;");

        JLabel labelSubtitle = new JLabel(subtitle);
        labelSubtitle.putClientProperty(FlatClientProperties.STYLE, "font: -1; foreground: #B0B0B0;");
    
        titlePanel.add(labelTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(labelSubtitle);

        add(titlePanel, BorderLayout.NORTH);

        JPanel actionBar = new JPanel(new BorderLayout(15, 0));
        actionBar.setOpaque(false);

        txtSearch = new  JTextField(28);
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por nome, CPF ou cargo...");
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 12; margin: 6,10,6,10; background: #2B2E32;");

        buttonCreate = new JButton(txtButton);
        buttonCreate.setFocusable(false);
        buttonCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCreate.setFont(buttonCreate.getFont().deriveFont(Font.BOLD, 13f));
        buttonCreate.setBackground(COR_LARANJA_PRIMARY);
        buttonCreate.setForeground(Color.WHITE);
        buttonCreate.putClientProperty(FlatClientProperties.STYLE, "arc: 12; margin: 8,16,8,16; borderWidth: 0;");

        if (clickNew != null)
            buttonCreate.addActionListener(e -> clickNew.run());
    
        actionBar.add(txtSearch, BorderLayout.WEST);
        actionBar.add(buttonCreate, BorderLayout.EAST);

        add(actionBar, BorderLayout.SOUTH);
    }    

    public JTextField getTextSearch() {
        return txtSearch;
    }

}
