package br.com.mercadinhoprovidence.view.component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Cursor;
import java.awt.Dimension;

import com.formdev.flatlaf.FlatClientProperties;

public class SideBarButton extends JButton {

    public SideBarButton(String message, Icon icon) {
        super(message);
        if (icon != null) {
            setIcon(icon);
            setIconTextGap(14);
        }

        setUpStyle();
    }

    /**
     * Método privado para applpicar o estilo do button.
     */
    private void setUpStyle() {
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        setHorizontalAlignment(SwingConstants.LEFT);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusable(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Estilo FlatLaf centralizado
        putClientProperty(FlatClientProperties.STYLE,
                "buttonType: toolbar; " +
                        "arc: 10; " +
                        "margin: 8,16,8,16; " +
                        "font: bold +1; " + 
                        "borderWidth: 0; " +
                        "focusWidth: 0; " +
                        "background: null; " +
                        "foreground: #111111; " + 
                        "disabledForeground: #888888; " + 
                        "hoverBackground: #f0f0f0; " +
                        "pressedBackground: #e0e0e0");
    }
}
