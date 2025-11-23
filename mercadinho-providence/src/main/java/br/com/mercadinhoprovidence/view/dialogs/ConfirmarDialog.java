package br.com.mercadinhoprovidence.view.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class ConfirmarDialog {

    private boolean confirmed = false;
    private Stage dialogStage;

    /**
     * Exibe um diálogo de confirmação para exclusão do banco de dados.
     *
     * @param ownerStage O Stage pai deste diálogo.
     * @param nome O nome do funcionário ou produto a ser exibido no diálogo.
     * @param tipo informa o o que está sendo deletado(produto ou funcionario).
     * @return true se o usuário confirmar a exclusão, false caso contrário.
     */
    public boolean show(Stage ownerStage, String nome, String tipo) {
        dialogStage = createDialogStage(ownerStage, tipo);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.getStyleClass().add("dialog-root");

        // Mensagem de confirmação
        Label messageLabel = new Label("Tem certeza que deseja excluir o " + tipo + "?");
        messageLabel.getStyleClass().add("dialog-message");

        Label nameLabel = new Label(nome);
        nameLabel.getStyleClass().add("dialog-name");

        Label warningLabel = new Label("Esta ação é irreversível!");
        warningLabel.getStyleClass().add("dialog-warning");

        // Botões de ação
        Button btnConfirm = new Button("Excluir");
        btnConfirm.setPrefWidth(100);
        btnConfirm.getStyleClass().add("dialog-btn-green");
        btnConfirm.setOnAction(e -> {
            confirmed = true;
            dialogStage.close();
        });

        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(100);
        btnCancel.getStyleClass().add("dialog-btn-red");
        btnCancel.setOnAction(e -> {
            confirmed = false;
            dialogStage.close();
        });

        HBox buttonBox = new HBox(15, btnCancel, btnConfirm); 
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(messageLabel, nameLabel, warningLabel, buttonBox);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/dialog.css")).toExternalForm());
        dialogStage.setScene(scene);
        dialogStage.sizeToScene(); 
        dialogStage.centerOnScreen();
        dialogStage.showAndWait(); 

        return confirmed;
    }

    /**
     * Metodo auxiliar para a criação da nossa dialogStage
     *
     * @param ownerStage recebe a nossa dialogStage.
     * @param tipo informa o que estamos deletando.
     *
     * @return um stage que será a tela principal de nossa aplicação.
     * */
    private Stage createDialogStage(Stage ownerStage, String tipo) {
        Stage stage = new Stage();

        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("Confirmar Exclusão" + tipo);
        return stage;
    }
}