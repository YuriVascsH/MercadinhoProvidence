package br.com.mercadinhoprovidence.view;

import br.com.mercadinhoprovidence.view.component.TitleComponents;
// import br.com.mercadinhoprovidence.view.panel.PainelEstoqueFX;
import br.com.mercadinhoprovidence.view.panel.PainelRelatorioVendasFX;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class RelatorioView {

    public static StackPane getView() {
        StackPane mainPane = new StackPane();
        mainPane.getStyleClass().add("main-pane");

        BorderPane contentLayout = new BorderPane();

        BorderPane headerPane = TitleComponents.crateHeadBorderPane("RELATÓRIOS DO MERCADINHO");
        headerPane.getStyleClass().add("header-pane");
        headerPane.setPadding(new Insets(10, 20, 10, 20));
        contentLayout.setTop(headerPane);


        // --- TabPane para as Abas de Relatório ---
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Aba Vendas
        Tab vendasTab = new Tab("VENDAS");
        vendasTab.setContent(PainelRelatorioVendasFX.getView());
        vendasTab.getStyleClass().add("tab-custom");

        // Aba Estoque
        Tab estoqueTab = new Tab("ESTOQUE");
        // estoqueTab.setContent(PainelEstoqueFX.getView());
        estoqueTab.getStyleClass().add("tab-custom");

        tabPane.getTabs().addAll(vendasTab, estoqueTab);
        contentLayout.setCenter(tabPane);
        mainPane.getChildren().add(contentLayout);

        mainPane.getStylesheets().add(
                RelatorioView.class.getResource("/css/relatorio-view.css").toExternalForm()
        );
        return mainPane;
    }
}