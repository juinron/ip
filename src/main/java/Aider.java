import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Aider chatbot application.
 */
public class Aider {
    /** Separates the chatbot's responses in the console. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** The banner displayed when the chatbot starts. */
    private static final String BANNER = "    _    ___ ____  _____ ____ \n"
            + "   / \\  |_ _|  _ \\| ____|  _ \\ \n"
            + "  / _ \\  | || | | |  _| | |_) |\n"
            + " / ___ \\ | || |_| | |___|  _ < \n"
            + "/_/   \\_\\___|____/|_____|_| \\_\\";

    /** The file path for persisting tasks. */
    private static final String DATA_FILE_PATH = "./data/duke.txt";

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Aider.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);

        Storage storage = new Storage(DATA_FILE_PATH);
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            tasks = storage.load();
        } catch (AiderException exception) {
            System.out.println("Could not load tasks from storage: " + exception.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            System.out.println(SEPARATOR);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                break;
            }

            boolean tasksChanged = false;

            try {
                if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                    tasksChanged = true;
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex));
                    tasksChanged = true;
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = getTaskIndex(command, "delete", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    tasksChanged = true;
                } else {
                    Task newTask = createTask(command);
                    tasks.add(newTask);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    tasksChanged = true;
                }
            } catch (AiderException exception) {
                System.out.println("OOPS!!! " + exception.getMessage());
            }

            if (tasksChanged) {
                try {
                    storage.save(tasks);
                } catch (AiderException exception) {
                    System.out.println("Could not save tasks to storage: " + exception.getMessage());
                }
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
    private static Task createTask(String command) throws AiderException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new AiderException("A todo needs a description, for example: todo read book.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String details = command.substring("deadline".length()).trim();
            int byIndex = details.indexOf(" /by ");
            if (byIndex < 0) {
                throw new AiderException("A deadline must include a description and a /by date or time.");
            }

            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + " /by ".length()).trim();
            if (description.isEmpty() || by.isEmpty()) {
                throw new AiderException("A deadline needs a description and a date or time after /by.");
            }
            return new Deadline(description, by);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            String details = command.substring("event".length()).trim();
            int fromIndex = details.indexOf(" /from ");
            int toIndex = details.indexOf(" /to ");
            if (fromIndex < 0 || toIndex <= fromIndex) {
                throw new AiderException("An event needs a description, /from time, and /to time.");
            }

            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + " /from ".length(), toIndex).trim();
            String to = details.substring(toIndex + " /to ".length()).trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new AiderException("An event needs text after its description, /from, and /to markers.");
            }
            return new Event(description, from, to);
        }

        throw new AiderException("I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or delete.");
    }

    /**
     * Parses and validates a task number from a mark or unmark command.
     *
     * @param command the complete command entered by the user
     * @param commandName the command name being parsed
     * @param taskCount the number of tasks currently stored
     * @return the zero-based index of the selected task
     * @throws AiderException if the command does not contain a valid task number
     */
    private static int getTaskIndex(String command, String commandName, int taskCount)
            throws AiderException {
        String taskNumberText = command.substring(commandName.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new AiderException("The " + commandName + " command needs a task number.");
        }

        final int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new AiderException("The " + commandName + " command needs a whole-number task number.");
        }

        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new AiderException("That task number does not exist.");
        }
        return taskIndex;
    }
}
