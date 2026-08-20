/**
 * Entry point for the Aider chatbot application.
 */
public class Aider {
    private static final String SEPARATOR = "____________________________________________________________";

    private static final String BANNER = "    _    ___ ____  _____ ____ \n"
            + "   / \\  |_ _|  _ \\| ____|  _ \\ \n"
            + "  / _ \\  | || | | |  _| | |_) |\n"
            + " / ___ \\ | || |_| | |___|  _ < \n"
            + "/_/   \\_\\___|____/|_____|_| \\_\\";

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Aider.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}
