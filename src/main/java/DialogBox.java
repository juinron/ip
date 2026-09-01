import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * A reusable conversation bubble for either the user or Aider.
 */
public class DialogBox extends HBox {
    /**
     * Creates a styled conversation bubble.
     *
     * @param message the text to display
     * @param isUser whether the message was sent by the user
     */
    public DialogBox(String message, boolean isUser) {
        Label text = new Label(message);
        text.setWrapText(true);
        text.setMaxWidth(400);
        text.setPadding(new Insets(10));
        getChildren().add(text);
        getStyleClass().add(isUser ? "user-dialog" : "duke-dialog");
        setPadding(new Insets(4, 8, 4, 8));
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(isUser ? javafx.geometry.Pos.CENTER_RIGHT : javafx.geometry.Pos.CENTER_LEFT);
    }
}
