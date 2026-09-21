package br.com.mercadinhoprovidence.view.component.global;

import javax.swing.JButton;

import com.formdev.flatlaf.FlatClientProperties;

import java.awt.Cursor;

public class SubmitButton extends JButton {

    public SubmitButton(String txt) {
        super(txt);
        setupStyle();
    }

    private void setupStyle() {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:#2ecc71;" +
                "foreground:#ffffff;" +
                "hoverBackground:darken(#2ecc71,10%);" +
                "pressedBackground:darken(#2ecc71,20%);" +
                "arc:10;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "font:bold +1");
    }

}
