package br.com.mercadinhoprovidence.util;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtils {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private TimeUtils() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    /**
     * Atualiza um JLabel do Swing com a data e hora atuais a cada segundo.
     *
     * @param label O JLabel do Swing a ser atualizado.
     */
    public static void startClock(JLabel label) {
        startClock(label, DEFAULT_FORMATTER);
    }

    /**
     * Atualiza um JLabel do Swing com a data e hora atuais em um formato customizado.
     *
     * @param label O JLabel do Swing a ser atualizado.
     * @param formatter Formato customizado para a data/hora.
     */
    public static void startClock(JLabel label, DateTimeFormatter formatter) {
        if (label == null || formatter == null) {
            return;
        }

        // Atualiza imediatamente antes de iniciar o timer
        label.setText(LocalDateTime.now().format(formatter));

        Timer timer = new Timer(1000, e -> label.setText(LocalDateTime.now().format(formatter)));
        timer.start();
    }
}