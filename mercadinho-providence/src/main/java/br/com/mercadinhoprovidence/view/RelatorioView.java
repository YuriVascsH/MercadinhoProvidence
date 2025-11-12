package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.view.components.ScreenTitle;
import br.com.mercadinhoprovidence.view.panel.PainelEstoqueFX;
import br.com.mercadinhoprovidence.view.panel.PainelRelatorioVendasFX;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class RelatorioView {

    public static StackPane getView() {
        StackPane mainPane = new StackPane();
        mainPane.setStyle("-fx-background-color: #F0F0F0;"); 
        BorderPane contentLayout = new BorderPane();

        BorderPane headerPane = ScreenTitle.crateHeadBorderPane("RELATÓRIOS DO MERCADINHO");
        headerPane.setPadding(new Insets(10, 20, 10, 20));
        headerPane.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");
        contentLayout.setTop(headerPane);


        // --- TabPane para as Abas de Relatório ---
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add("floating"); 

        // Aba Vendas
        Tab vendasTab = new Tab("VENDAS");
        vendasTab.setContent(PainelRelatorioVendasFX.getView());
        vendasTab.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");

        // Aba Estoque
        Tab estoqueTab = new Tab("ESTOQUE");
        estoqueTab.setContent(PainelEstoqueFX.getView());
        estoqueTab.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        tabPane.getTabs().addAll(vendasTab, estoqueTab);

        contentLayout.setCenter(tabPane);
        mainPane.getChildren().add(contentLayout);

        return mainPane;
    }
}