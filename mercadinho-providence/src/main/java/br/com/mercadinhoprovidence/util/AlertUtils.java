package br.com.mercadinhoprovidence.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Classe utilitária para exibir diferentes tipos de alertas na interface
 * gráfica do Swing. Simplifica a criação e exibição de mensagens
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
    private static void showAlert(String title, String header, String message, int messageType) {
        String fullMessage = formatMessage(header, message);

        if (SwingUtilities.isEventDispatchThread()) {
            JOptionPane.showMessageDialog(null, fullMessage, title, messageType);
        } else {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, fullMessage, title, messageType));
        }
    }

    /**
     * Formata mensagem para incluir cabeçalho, se existir.
     */
    private static String formatMessage(String header, String message) {
        if (header != null && !header.isBlank()) {
            return header + "\n\n" + message;
        }
        return message;
    }

    public static void showError(String title, String message) {
        showAlert(title, null, message, JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(String title, String header, String message) {
        showAlert(title, header, message, JOptionPane.ERROR_MESSAGE);
    }

    // --- MÉTODOS DE AVISO ---

    public static void showWarning(String title, String message) {
        showAlert(title, null, message, JOptionPane.WARNING_MESSAGE);
    }

    // --- MÉTODOS DE SUCESSO / INFORMAÇÃO ---

    public static void showSuccess(String title, String message) {
        showAlert(title, null, message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showSuccess(String title, String header, String message) {
        showAlert(title, header, message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showInfo(String title, String message) {
        showAlert(title, null, message, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exibe um alerta de CONFIRMAÇÃO (Sim / Não).
     * 
     * @return true se o usuário clicou em SIM (OK), false caso contrário.
     */
    public static boolean showConfirmation(String title, String message) {
        return showConfirmation(title, null, message);
    }

    /**
     * Exibe um alerta de CONFIRMAÇÃO com cabeçalho.
     * 
     * @return true se o usuário clicou em SIM (OK), false caso contrário.
     */
    public static boolean showConfirmation(String title, String header, String message) {
        String fullMessage = formatMessage(header, message);

        int option = JOptionPane.showConfirmDialog(
                null,
                fullMessage,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return option == JOptionPane.YES_OPTION;
    }
}
