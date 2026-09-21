package br.com.mercadinhoprovidence.view.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.mercadinhoprovidence.util.ImagemUtil;

public class SideBar extends JPanel {

    private JLabel logoLabel;
    private Set<String> buttonsDisabled;
    private Map<String, String> buttonsConfig;
    private final Map<String, JButton> botoesInstanciados = new HashMap<>();
    private SideBarButton exitButton;

    public SideBar(Map<String, String> buttonsConfig, Set<String> buttonsDisabled) {
        this.buttonsConfig = buttonsConfig;
        this.buttonsDisabled = buttonsDisabled;
        setupLayout();
        buildHeader();
        buildMenu();

    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(240, 0));
        putClientProperty(FlatClientProperties.STYLE, "background: #f27a1f");
    }

    private void buildHeader() {
        logoLabel = new LogoLabel(ImagemUtil.loadImage(getClass(), "/images/logoMercado.png", 240, 160));
        add(logoLabel, BorderLayout.NORTH);
    }

    private void buildMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 15));
        menuPanel.setOpaque(false);

        buttonsConfig.forEach((texto, caminhoIcone) -> {
            if (!texto.equalsIgnoreCase("Sair")) {
                SideBarButton btn = new SideBarButton(texto, ImagemUtil.loadImage(getClass(), caminhoIcone, 22, 22));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);

                if (buttonsDisabled != null && buttonsDisabled.contains(texto)) {
                    btn.setEnabled(false);
                }

                botoesInstanciados.put(texto, btn);
                menuPanel.add(btn);

                // Espaçamento entre os botões (30px)
                menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            }
        });

        // Empurra o botão "Sair" para o rodapé
        menuPanel.add(Box.createVerticalGlue());

        // --- Botão Sair ---
        if (buttonsConfig.containsKey("Sair")) {
            JButton btnSair = new DangerSideBarButton(
                    "Sair",
                    ImagemUtil.loadImage(getClass(), buttonsConfig.get("Sair"), 22, 22));
            btnSair.setAlignmentX(Component.CENTER_ALIGNMENT);

            botoesInstanciados.put("Sair", btnSair);
            menuPanel.add(btnSair);
        }

        add(menuPanel, BorderLayout.CENTER);
    }

    /**
     * Registra uma ação de clique para um botão específico pelo texto/chave.
     */
    public void setButtonAction(String textoBotao, ActionListener action) {
        JButton btn = botoesInstanciados.get(textoBotao);
        if (btn != null) {
            btn.addActionListener(action);
        }
    }

    /**
     * Assina uma ação padrão para todos os botões no formato (texto, botao).
     */
    public void setOnButtonClick(BiConsumer<String, JButton> handler) {
        botoesInstanciados.forEach((texto, btn) -> {
            btn.addActionListener(e -> handler.accept(texto, btn));
        });
    }

    public JButton getButton(String textoBotao) {
        return botoesInstanciados.get(textoBotao);
    }
}