package wcinterface.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertWindow {

    public static final String MESSAGE = "- Выключите, пожалуйста, включенные мониторы COM-портов;\n" +
            "- Если ошибка COM-port busy не убирается, попробуйте подключить плату заново;\n" +
            "- Убедитесь, пожалуйста, что веб-камера подключена\n" +
            "- При поднесении на нужную дистанцию, на плате загорится зеленый светодиод, после чего будет сделано фото\n" +
            "- Фото сохраняются в корне\n";

    public static void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Важно!");
        window.setResizable(false);
        window.setWidth(300);
        window.setHeight(300);

        Label text = new Label(MESSAGE);
        text.setWrapText(true);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(text);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.show();
    }
}
