package br.com.mercadinhoprovidence.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TitleHeaderPane {

    /**
     * Cria e retorna um VBox(Contendo o título da aplicação) que serve como o cabeçalho.
     * @return VBox contendo o título.
     */
    public static VBox createHeaderBox() {
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setId("header-box");
        Label titleLabel = new Label("Mercadinho Providence");
        headerBox.getChildren().add(titleLabel);
        return headerBox;
    }
    
}
