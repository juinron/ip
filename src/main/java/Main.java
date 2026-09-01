import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Provides the graphical user interface for Aider.
 */
public class Main extends Application {
    /** The conversation history displayed in the window. */
    private final VBox dialogContainer = new VBox(8);

    /** The field in which the user enters commands. */
    private final TextField userInput = new TextField();

    /** The button used to submit a command. */
    private final Button sendButton = new Button("Send");

    /**
     * Builds and displays the Aider window.
     *
     * @param stage the primary JavaFX window
     */
    @Override
    public void start(Stage stage) {
        ScrollPane conversation = new ScrollPane(dialogContainer);
        conversation.setFitToWidth(true);
        conversation.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        BorderPane layout = new BorderPane();
        layout.setCenter(conversation);

        HBox inputBar = new HBox(8, userInput, sendButton);
        inputBar.setPadding(new Insets(8));
        HBox.setHgrow(userInput, javafx.scene.layout.Priority.ALWAYS);
        layout.setBottom(inputBar);

        sendButton.setOnAction(event -> submitCommand());
        userInput.setOnAction(event -> submitCommand());
        addDukeMessage("Hello! I'm Aider. What can I do for you?");

        Scene scene = new Scene(layout, 520, 640);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Aider");
        stage.setScene(scene);
        stage.show();
    }

    /** Sends the entered command and appends both sides of the exchange. */
    private void submitCommand() {
        String command = userInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        addUserMessage(command);
        userInput.clear();
        try {
            String response = Aider.processCommand(command);
            addDukeMessage(response);
            if (command.equals("bye")) {
                sendButton.setDisable(true);
                userInput.setDisable(true);
            }
        } catch (AiderException exception) {
            addDukeMessage("OOPS!!! " + exception.getMessage());
        }
    }

    /** Adds a user message to the conversation. */
    private void addUserMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox(message, true));
    }

    /** Adds an Aider message to the conversation. */
    private void addDukeMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox(message, false));
    }
}
