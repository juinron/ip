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

                boolean changed = false;
                try {
                    if (command.equals("list")) {
                        ui.showTasks(tasks);
                    } else if (command.equals("on") || command.startsWith("on ")) {
                        LocalDate date = parser.parseDate(command);
                        ui.showTasksOnDate(date, tasks.occurringOn(date));
                    } else if (command.equals("mark") || command.startsWith("mark ")) {
                        ui.showMarked(tasks.mark(command));
                        changed = true;
                    } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                        ui.showUnmarked(tasks.unmark(command));
                        changed = true;
                    } else if (command.equals("delete") || command.startsWith("delete ")) {
                        ui.showDeleted(tasks.remove(tasks.indexOf(command, "delete")), tasks.size());
                        changed = true;
                    } else {
                        Task task = parser.parseTask(command);
                        tasks.add(task);
                        ui.showAdded(task, tasks.size());
                        changed = true;
                    }
                } catch (AiderException exception) {
                    ui.showError(exception.getMessage());
                }

                if (changed) {
                    try {
                        storage.save(tasks.asList());
                    } catch (AiderException exception) {
                        ui.showSavingError(exception.getMessage());
                    }
                }
                ui.showSeparator();
            }
        }
    }

    /** Starts Aider with its project-relative data file. */
    public static void main(String[] args) {
        new Aider(DATA_FILE_PATH).run();
    }
}
