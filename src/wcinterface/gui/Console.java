package wcinterface.gui;

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

import static wcinterface.gui.GUIUtils.runSafe;

public class Console extends BorderPane {

    private final TextArea textArea = new TextArea();

    public Console() {
        textArea.setEditable(false);
        setCenter(textArea);
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
    }

    public void clear() {
        runSafe(textArea::clear);
    }

    public void print(final String text) {
        Objects.requireNonNull(text, "text");
        runSafe(() -> textArea.appendText(text));
    }

    public void println(final String text) {
        Objects.requireNonNull(text, "text");
        runSafe(() -> textArea.appendText(text + System.lineSeparator()));
    }

    public void newLine() {
        runSafe(() -> textArea.appendText(System.lineSeparator()));
    }

    public void printError(final String error) {
        Objects.requireNonNull(error, "error");
        runSafe(() -> textArea.appendText("ERROR: " + error));
    }

    public void printlnError(final String error) {
        printError(error);
        newLine();
    }
}
