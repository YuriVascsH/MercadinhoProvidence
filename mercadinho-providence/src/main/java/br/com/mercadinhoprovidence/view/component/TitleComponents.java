package br.com.mercadinhoprovidence.view.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TitleComponents {

    /**
     * Método para que cria uma BorderPane que vai retornar o título
     * @param titleName recebe o nome da seção que será adicinada na label
     * @return uma BordPane contendo uma linha e o título da da tela a esquerda
     */
    public static BorderPane crateHeadBorderPane(String titleName){
        Label titulo = new Label(titleName);
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Line linhaDecorativa = new Line();
        linhaDecorativa.setStartX(0);
        linhaDecorativa.setEndX(150);
        linhaDecorativa.setStroke(Color.web("#E65100"));
        linhaDecorativa.setStrokeWidth(3);

        VBox tituloComLinha = new VBox(5);
        tituloComLinha.setAlignment(Pos.CENTER_LEFT);
        tituloComLinha.getChildren().addAll(titulo, linhaDecorativa);

        BorderPane topoTitulo = new BorderPane();
        topoTitulo.setLeft(tituloComLinha);

        BorderPane.setMargin(tituloComLinha, new Insets(0, 0, 10, 0));

        return topoTitulo;
    }

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
