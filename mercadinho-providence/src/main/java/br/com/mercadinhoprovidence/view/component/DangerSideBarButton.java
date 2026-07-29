package br.com.mercadinhoprovidence.view.component;

import javax.swing.Icon;

import com.formdev.flatlaf.FlatClientProperties;

public class DangerSideBarButton extends SideBarButton {

    public DangerSideBarButton(String message, Icon icon) {
        super(message, icon);
        setupDangerStyle();
    }

    private void setupDangerStyle() {
        putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10; " +
            "margin: 8,12,8,12; " +
            "font: medium +1; " +
            "borderWidth: 0; " +
            "background: #f5f5f5; " +
            "foreground: #333333; " +
            "hoverBackground: #dc3545; " + // Vermelho ao passar o mouse
            "hoverForeground: #ffffff"
        );
    }
    
}
