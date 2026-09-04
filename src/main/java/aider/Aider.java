package aider;

import java.time.LocalDate;

import aider.model.Task;
import aider.model.TaskList;
import aider.storage.Storage;

/** Coordinates the user interface, parser, task list, and storage. */
public class Aider {
    /** Default path used when Aider is launched from the project root. */
    private static final String DATA_FILE_PATH = "./data/duke.txt";

    /** Persists tasks between application runs. */
    private final Storage storage;

    /** Stores the tasks managed during the current run. */
    private final TaskList tasks;

    /** Displays responses and prompts to the user. */
    private final Ui ui;

    /** Converts user commands into task operations. */
    private final Parser parser;

    /** Creates Aider and loads any previously saved tasks. */
    public Aider(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (AiderException exception) {
            ui.showLoadingError(exception.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /** Runs the command loop until the user exits. */
    public void run() {
        ui.showWelcome();
        try (ui) {
            while (ui.hasNextCommand()) {
                String command = ui.readCommand();
                if (command.equals("bye")) {
                    ui.showGoodbye();
                    break;
                }
                if (command.isEmpty()) {
                    continue;
                }

                try {
                    System.out.println(processCommand(command));
                } catch (AiderException exception) {
                    ui.showError(exception.getMessage());
                }
                ui.showSeparator();
            }
        }
    }

    /**
     * Processes one command and returns the text that should be shown to a user.
     * This method is shared by the command-line and graphical interfaces.
     *
     * @param command the command entered by the user
     * @return the command response
     * @throws AiderException if the command is invalid or changes cannot be saved
     */
    public String processCommand(String command) throws AiderException {
        if (command.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }

        if (command.equals("list")) {
            StringBuilder response = new StringBuilder("Here are the tasks in your list:");
            if (tasks.isEmpty()) {
                return response.append("\n  (no tasks yet)").toString();
            }
            for (int i = 0; i < tasks.size(); i++) {
                response.append("\n").append(i + 1).append(".").append(tasks.get(i));
            }
            return response.toString();
        }

        if (command.equals("find") || command.startsWith("find ")) {
            return formatTasks("Here are the matching tasks:", tasks.find(command), "no matching tasks");
        }

        if (command.equals("on") || command.startsWith("on ")) {
            LocalDate date = parser.parseDate(command);
            return formatTasks("Tasks on " + date + ":", tasks.occurringOn(date),
                    "no deadlines or events");
        }

        boolean changed = false;
        String response;
        if (command.equals("mark") || command.startsWith("mark ")) {
            Task task = tasks.mark(command);
            response = "Nice! I've marked this task as done:\n  " + task;
            changed = true;
        } else if (command.equals("unmark") || command.startsWith("unmark ")) {
            Task task = tasks.unmark(command);
            response = "OK, I've marked this task as not done yet:\n  " + task;
            changed = true;
        } else if (command.equals("delete") || command.startsWith("delete ")) {
            Task task = tasks.remove(tasks.indexOf(command, "delete"));
            response = "Noted. I've removed this task:\n  " + task
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
            changed = true;
        } else {
            Task task = parser.parseTask(command);
            tasks.add(task);
            response = "Got it. I've added this task:\n  " + task
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
            changed = true;
        }

        if (changed) {
            try {
                storage.save(tasks.asList());
            } catch (AiderException exception) {
                throw new AiderException("Could not save the data file: " + exception.getMessage());
            }
        }
        return response;
    }

    /** Formats a list response shared by search and date queries. */
    private static String formatTasks(String heading, java.util.ArrayList<Task> matchingTasks,
            String emptyMessage) {
        StringBuilder response = new StringBuilder(heading);
        if (matchingTasks.isEmpty()) {
            return response.append("\n  (").append(emptyMessage).append(")").toString();
        }
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append("\n").append(i + 1).append(".").append(matchingTasks.get(i));
        }
        return response.toString();
    }

    /** Starts Aider with its project-relative data file. */
    public static void main(String[] args) {
        new Aider(DATA_FILE_PATH).run();
    }
}
