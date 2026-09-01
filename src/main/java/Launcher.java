import javafx.application.Application;

/**
 * Launches the JavaFX application without directly extending {@link Application}.
 */
public class Launcher {
    /**
     * Starts the Aider graphical user interface.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
