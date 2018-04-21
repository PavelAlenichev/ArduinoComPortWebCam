package wcinterface;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import wcinterface.gui.AlertWindow;
import wcinterface.gui.Console;
import wcinterface.logic.ReadComPort;

import java.io.File;

public class Main extends Application {

    private Stage window;
    private GridPane gridPane;
    private Button startAction, stopAction, clearConsole;
    private Console console;
    private ImageView imageView;

    private static final int WINDOW_PADDING = 20;
    private static final String TITLE = "Arduino WebCam observer";
    private static final int HEIGHT = 400;
    private static final int WIDTH = 600;


    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle(TITLE);

        initGradPane();
        initChildren();
        initScene();
        setListeners();

        window.setResizable(false);
        window.show();
        AlertWindow.display();
    }

    private void initGradPane() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(WINDOW_PADDING, WINDOW_PADDING, WINDOW_PADDING, WINDOW_PADDING));
        gridPane.setVgap(WINDOW_PADDING);
        gridPane.setHgap(WINDOW_PADDING);
    }

    private void initChildren() {
        startAction = new Button("Start activity!");
        GridPane.setConstraints(startAction, 0, 0);

        stopAction = new Button("Stop");
        GridPane.setConstraints(stopAction, 1, 0);

        clearConsole = new Button("Clear console");
        GridPane.setConstraints(clearConsole, 2, 0);

        console = new Console();
        ReadComPort.setConsole(console);
        GridPane.setConstraints(console, 0, 1, 2, 1);

        initImageView();
        GridPane.setConstraints(imageView, 0, 2);

        gridPane.getChildren().addAll(startAction, stopAction, clearConsole, console, imageView);
    }

    private void initImageView() {
        imageView = new ImageView();
        int latestPhoto = 0;
        File file;
        for (int i = 1; i < 1000; i++) {
            file = new File(i + ".png");
            if (!file.exists()) {
                latestPhoto = --i;
                break;
            }
        }
        File latestFile = new File(latestPhoto + ".png");
        Image image = new Image(latestFile.toURI().toString());
        imageView.setImage(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
    }

    private void initScene() {
        Scene scene = new Scene(gridPane, WIDTH, HEIGHT);
        window.setScene(scene);
    }

    private void setListeners() {
        startAction.setOnAction(event -> ReadComPort.init());
        stopAction.setOnAction(event -> ReadComPort.detach());
        clearConsole.setOnAction(event -> console.clear());

        ReadComPort.setListener(count -> {
            File file = new File(count + ".png");
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
