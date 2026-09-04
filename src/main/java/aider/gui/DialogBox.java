package aider.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Represents one user or Aider message in the conversation. */
public final class DialogBox extends HBox {
    /** Creates a conversation bubble. */
    public DialogBox(String message, boolean isUser) {
        Label text = new Label(message);
        text.setWrapText(true);
        text.setMaxWidth(400);
        text.setPadding(new Insets(10));
        getChildren().add(text);
        getStyleClass().add(isUser ? "user-dialog" : "aider-dialog");
        setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        setMaxWidth(Double.MAX_VALUE);
        setPadding(new Insets(4, 8, 4, 8));
    }
}
