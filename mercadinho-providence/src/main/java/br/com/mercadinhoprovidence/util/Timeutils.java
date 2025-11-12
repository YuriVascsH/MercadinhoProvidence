package br.com.mercadinhoprovidence.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Timeutils {

    /**
         * Atualiza o Label com a data e hora atuais a cada segundo.
         * @param label O Label a ser atualizado.
         */
    public static void updateDateTime(Label label) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), event ->{
                        String currentDateTime = LocalDateTime.now().format(formatter);
                        label.setText(currentDateTime);
                    })
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        }
}
