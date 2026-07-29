package br.com.mercadinhoprovidence.view.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.mercadinhoprovidence.util.ImagemUtil;

public class SideBar extends JPanel {

    private JLabel logoLabel;
    private JPanel menuPanel;
    private Set<String> buttonsDesabilit;
    private Map<String, String> buttonsConfig;
    private SideBarButton exitButton;

    public SideBar(Map<String, String> buttonsConfig, Set<String>buttonsDesabilit) {
        this.buttonsConfig = buttonsConfig; 
        this.buttonsDesabilit = buttonsDesabilit;
        setupLayout();
        buildHeader();

    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(240, 0));
        putClientProperty(FlatClientProperties.STYLE, "background: #FF0000");
    }

    private void buildHeader() {
        logoLabel = new LogoLabel(ImagemUtil.loadImage(getClass(), "/images/logoMercado.png", 240, 160));
        add(logoLabel, BorderLayout.NORTH);

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 15));
        menuPanel.setOpaque(false);

        buttonsConfig.forEach((text, pathIcon) -> {
            if (!text.equalsIgnoreCase("Sair")) {
                exitButton = new SideBarButton(pathIcon, ImagemUtil.loadImage(getClass(), pathIcon, 22, 22));
                exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                if(buttonsDesabilit.contains(text)) {
                    exitButton.setEnabled(false);
                }

                
                add(exitButton);


                
            }
        });

    }
    

}
