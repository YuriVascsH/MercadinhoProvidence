package br.com.mercadinhoprovidence.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EstoqueView {
    
    public static StackPane getView(){
        StackPane pane = new StackPane();

        BorderPane headerSection = createSectionTitle("Gestão de Estoque");

        VBox vboxConteudo = new VBox(10);
        vboxConteudo.getChildren().addAll(headerSection);

        pane.getChildren().add(vboxConteudo);


        return pane;
    }


    /**
     * 
     * @param titleText
     * @return
     */
    public static BorderPane createSectionTitle(String titleText) {
        BorderPane headerSection = new BorderPane();
        headerSection.setPadding(new Insets(10, 0, 10, 0));

        Label titleLabel = new Label(titleText); 
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Line lineDecorative = new Line();
        lineDecorative.setStartX(0);
        
        lineDecorative.setStroke(Color.web("#E65100"));
        lineDecorative.setStrokeWidth(3);

        VBox titleAndLine = new VBox(5); 
        titleAndLine.setAlignment(Pos.CENTER_LEFT);
        titleAndLine.getChildren().addAll(titleLabel, lineDecorative);

        HBox wrapper = new HBox();
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.getChildren().add(titleAndLine);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        wrapper.getChildren().add(spacer);

        wrapper.widthProperty().addListener((obs, oldVal, newVal) -> {
            lineDecorative.setEndX(newVal.doubleValue());
        });

        headerSection.setLeft(wrapper); 

        return headerSection; 
    }

    

}
