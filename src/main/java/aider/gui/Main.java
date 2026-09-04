package aider.gui;

import aider.Aider;
import aider.AiderException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Provides the graphical user interface for Aider. */
public final class Main extends javafx.application.Application {
    /** Displays the conversation history. */
    private final VBox dialogContainer = new VBox(8);

    /** Accepts commands from the user. */
    private final TextField userInput = new TextField();

    /** Sends the command in the input field. */
    private final Button sendButton = new Button("Send");

    /** Connects the GUI to the existing application logic. */
    private final Aider aider = new Aider("./data/duke.txt");

    /** Builds and displays the application window. */
    @Override
    public void start(Stage stage) {
        ScrollPane conversation = new ScrollPane(dialogContainer);
        conversation.setFitToWidth(true);
        conversation.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        BorderPane layout = new BorderPane(conversation);
        HBox inputBar = new HBox(8, userInput, sendButton);
        inputBar.setPadding(new Insets(8));
        HBox.setHgrow(userInput, Priority.ALWAYS);
        layout.setBottom(inputBar);

        sendButton.setOnAction(event -> submitCommand());
        userInput.setOnAction(event -> submitCommand());
        addAiderMessage("Hello! I'm Aider. What can I do for you?");

        Scene scene = new Scene(layout, 520, 640);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Aider");
        stage.setScene(scene);
        stage.show();
    }

    /** Sends the entered command and displays the response. */
    private void submitCommand() {
        String command = userInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }
        addUserMessage(command);
        userInput.clear();
        try {
            addAiderMessage(aider.processCommand(command));
            if (command.equals("bye")) {
                sendButton.setDisable(true);
                userInput.setDisable(true);
            }
        } catch (AiderException exception) {
            addAiderMessage("OOPS!!! " + exception.getMessage());
        }
    }

    /** Adds a user message to the conversation. */
    private void addUserMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox(message, true));
    }

    /** Adds an Aider message to the conversation. */
    private void addAiderMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox(message, false));
    }
}
