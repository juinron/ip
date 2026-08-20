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

        Task[] tasks = new Task[MAX_TASKS];
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
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumberText = command.substring("mark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    int taskIndex = taskNumber - 1;

                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[taskIndex]);
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
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[taskIndex]);
                    } else {
                        System.out.println("That task number does not exist.");
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (taskCount < MAX_TASKS) {
                Task newTask = createTask(command);
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
            } else {
                System.out.println("Task limit reached.");
            }

            System.out.println(SEPARATOR);
        }
    }

    /**
     * Creates the appropriate task subtype from a user command.
     *
     * @param command the complete command entered by the user
     * @return the task represented by the command
     */
    private static Task createTask(String command) {
        if (command.startsWith("todo ")) {
            return new Todo(command.substring("todo ".length()));
        }

        if (command.startsWith("deadline ")) {
            String details = command.substring("deadline ".length());
            int byIndex = details.indexOf(" /by ");
            if (byIndex >= 0) {
                String description = details.substring(0, byIndex);
                String by = details.substring(byIndex + " /by ".length());
                return new Deadline(description, by);
            }
        }

        if (command.startsWith("event ")) {
            String details = command.substring("event ".length());
            int fromIndex = details.indexOf(" /from ");
            int toIndex = details.indexOf(" /to ");
            if (fromIndex >= 0 && toIndex > fromIndex) {
                String description = details.substring(0, fromIndex);
                String from = details.substring(fromIndex + " /from ".length(), toIndex);
                String to = details.substring(toIndex + " /to ".length());
                return new Event(description, from, to);
            }
        }

        return new Todo(command);
    }
}
