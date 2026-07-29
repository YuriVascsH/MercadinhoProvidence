package br.com.mercadinhoprovidence.view.component;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

public class LogoLabel extends JLabel {

    public LogoLabel(Icon icon) {
        if (icon != null)
            setIcon(icon);
        else
            setupStyle();
    }

    /**
     * Estilização padrão aplicada APENAS se a imagem não for carregada.
     */
    private void setupStyle() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setText("MERCADINHO");
        setPreferredSize(new Dimension(240, 80));
        putClientProperty(FlatClientProperties.STYLE, "font: bold +6; foreground: #333333");

    }

}
