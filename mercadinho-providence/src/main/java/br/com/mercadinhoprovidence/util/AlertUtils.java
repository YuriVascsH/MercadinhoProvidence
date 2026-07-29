package br.com.mercadinhoprovidence.util;

import javax.swing.JOptionPane;

/**
 * Classe utilitária para exibir diferentes tipos de alertas na interface gráfica
 * usando Swing e com o visual moderno herdado do FlatLaf.
 */
public class AlertUtils {

    /**
     * Método privado genérico para criar e exibir os alertas informativos/erro do Swing.
     * 
     * @param messageType O tipo da mensagem do Swing (JOptionPane.ERROR_MESSAGE, etc.)
     * @param title       O título da janela do alerta.
     * @param header      O texto do cabeçalho (Opcional, em negrito).
     * @param message     A mensagem principal do alerta.
     */
    private static void showAlert(int messageType, String title, String header, String message) {
        Object formattedMessage;

        // Se houver um cabeçalho, usamos tags HTML para dar o destaque que o JavaFX dava
        if (header != null && !header.trim().isEmpty()) {
            formattedMessage = "<html><b><font size='4'>" + header + "</font></b><br><br>" + message;
        } else {
            formattedMessage = message;
        }

        JOptionPane.showMessageDialog(null, formattedMessage, title, messageType);
    }

    /**
     * Exibe um alerta de ERRO.
     */
    public static void showError(String title, String message) {
        showAlert(JOptionPane.ERROR_MESSAGE, title, null, message);
    }

    /**
     * Exibe um alerta de ERRO com um cabeçalho personalizado.
     */
    public static void showError(String title, String header, String message) {
        showAlert(JOptionPane.ERROR_MESSAGE, title, header, message);
    }

    /**
     * Exibe um alerta de AVISO.
     */
    public static void showWarning(String title, String message) {
        showAlert(JOptionPane.WARNING_MESSAGE, title, null, message);
    }

    /**
     * Exibe um alerta de SUCESSO (Informação).
     */
    public static void showSuccess(String title, String message) {
        showAlert(JOptionPane.INFORMATION_MESSAGE, title, null, message);
    }

    /**
     * Exibe um alerta de SUCESSO com um cabeçalho personalizado.
     */
    public static void showSuccess(String title, String header, String message) {
        showAlert(JOptionPane.INFORMATION_MESSAGE, title, header, message);
    }

    /**
     * Exibe um alerta de INFORMAÇÃO.
     */
    public static void showInfo(String title, String message) {
        showAlert(JOptionPane.INFORMATION_MESSAGE, title, null, message);
    }

    /**
     * Exibe um alerta de CONFIRMAÇÃO (Sim/Não).
     * 
     * @return true se o usuário clicou em SIM, false se clicou em NÃO ou fechou a janela.
     */
    public static boolean showConfirmation(String title, String message) {
        return showConfirmation(title, null, message);
    }

    /**
     * Exibe um alerta de CONFIRMAÇÃO com um cabeçalho personalizado (Sim/Não).
     * 
     * @return true se o usuário clicou em SIM, false se clicou em NÃO ou fechou a janela.
     */
    public static boolean showConfirmation(String title, String header, String message) {
        Object formattedMessage;
        
        if (header != null && !header.trim().isEmpty()) {
            formattedMessage = "<html><b><font size='4'>" + header + "</font></b><br><br>" + message + "</html>";
        } else {
            formattedMessage = message;
        }

        // Exibe uma caixa com opções "Sim" e "Não"
        int result = JOptionPane.showConfirmDialog(
                null, 
                formattedMessage, 
                title, 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }
}