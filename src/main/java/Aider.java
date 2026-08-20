import java.util.Scanner;

/**
 * Entry point for the Aider chatbot application.
 */
public class Aider {
    /** Maximum number of tasks that can be stored in memory. */
    private static final int MAX_TASKS = 100;

    /** Separates the chatbot's responses in the console. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** The banner displayed when the chatbot starts. */
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

        String[] tasks = new String[MAX_TASKS];
        boolean[] completed = new boolean[MAX_TASKS];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                break;
            }

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    String status = completed[i] ? "X" : " ";
                    System.out.println((i + 1) + ".[" + status + "] " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumberText = command.substring("mark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    int taskIndex = taskNumber - 1;

                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        completed[taskIndex] = true;
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  [X] " + tasks[taskIndex]);
                    } else {
                        System.out.println("That task number does not exist.");
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (command.startsWith("unmark ")) {
                String taskNumberText = command.substring("unmark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    int taskIndex = taskNumber - 1;

                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        completed[taskIndex] = false;
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  [ ] " + tasks[taskIndex]);
                    } else {
                        System.out.println("That task number does not exist.");
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            } else {
                System.out.println("Task limit reached.");
            }

            System.out.println(SEPARATOR);
        }
    }
}
