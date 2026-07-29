package br.com.mercadinhoprovidence.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Classe utilitária para exibir diferentes tipos de alertas na interface
 * gráfica
 * da aplicação JavaFX. Simplifica a criação e exibição de mensagens
 * padronizadas.
 */
public class AlertUtils {

    /**
     * Método privado genérico para criar e exibir um alerta
     * Centraliza a lógica comum de exibição de alertas
     * 
     * @param type    O tipo do alerta (ERROR, WARNING, INFORMATION, CONFIRMATION)
     * @param title   O título da janela do alerta.
     * @param header  O texto do cabeçalho do alerta (Pode ser null para não ser
     *                exibida)
     * @param message A mesnagem principal do alerta
     */
    private static void showAlert(AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de ERRO.
     *
     * @param title   O título da janela do alerta.
     * @param message A mensagem de erro.
     */
    public static void showError(String title, String message) {
        showAlert(AlertType.ERROR, title, null, message);
    }

    /**
     * Exibe um alerta de ERRO com um cabeçalho personalizado.
     * 
     * @param title   O título do alerta.
     * @param header  O cabeçalho do alerta
     * @param message A mensagem de erro.
     */
    public static void showError(String title, String header, String message) {
        showAlert(AlertType.ERROR, title, header, message);
    }

    /**
     * Exibe um alerta de AVISO.
     * 
     * @param title   O título da janela do alerta.
     * @param message A mensagem do aviso.
     */
    public static void showWarning(String title, String message) {
        showAlert(AlertType.WARNING, title, null, message);

    }

    /**
     * Exibe um alerta de SUCESSO.
     * 
     * @param title   O título da janela aberta.
     * @param message A mensagem do sucesso.
     */
    public static void showSuccess(String title, String message) {
        showAlert(AlertType.INFORMATION, title, null, message);
    }

    /**
     * Exibre uma alerta de SUCESSO com um cabeçalho personalizado.
     * 
     * @param title   O título do janela do alerta.
     * @param header  O cabeçalho do alerta.
     * @param message A mensagem do sucesso.
     */
    public static void showSuccess(String title, String header, String message) {
        showAlert(AlertType.INFORMATION, title, header, message);
    }

    /**
     * Exibe um alerta de INFORMÇÃO.
     * 
     * @param title   O título da janela do alerta.
     * @param message A mensagem de informação.
     */
    public static void showInfo(String title, String message) {
        showAlert(AlertType.INFORMATION, title, null, message);
    }

    /**
     * Exibe um alerta de CONFIRMAÇÃO, que geralmente inclui botões como "OK" e
     * "Cancelar".
     *
     * @param title   O título da janela do alerta.
     * @param message A mensagem de confirmação.
     * @return true se o usuário clicou em OK, false caso contrário (ou se fechou a
     *         janela).
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /**
     * Exibe um alerta de CONFIRMAÇÃO com um cabeçalho personalizado.
     *
     * @param title   O título da janela do alerta.
     * @param header  O cabeçalho do alerta.
     * @param message A mensagem de confirmação.
     * @return true se o usuário clicou em OK, false caso contrário (ou se fechou a
     *         janela).
     */
    public static boolean showConfirmation(String title, String header, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /**
     * Exibe um alerta de CONFIRMAÇÃO e retorna o resultado completo.
     *
     * @param title   O título da janela do alerta.
     * @param header  O cabeçalho do alerta.
     * @param message A mensagem de confirmação.
     * @return Optional com o botão clicado pelo usuário.
     */
    public static Optional<ButtonType> showConfirmationAndGetResult(String title, String header, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    

}
