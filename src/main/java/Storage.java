import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading tasks from and saving tasks to the hard disk.
 */
public class Storage {
    /** The relative path of the file used to persist tasks. */
    private final String filePath;

    /**
     * Creates a storage handler that uses the given file path.
     *
     * @param filePath the relative path of the data file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file. If the file or its parent directory does not exist,
     * an empty list is returned.
     *
     * @return the list of tasks loaded from disk
     * @throws AiderException if a line in the file cannot be parsed
     */
    public ArrayList<Task> load() throws AiderException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    tasks.add(parseLine(line));
                }
            }
        } catch (IOException exception) {
            throw new AiderException("Could not read the data file: " + exception.getMessage());
        }
        return tasks;
    }

    /**
     * Saves the given tasks to the data file, creating the parent directory if needed.
     *
     * @param tasks the tasks to persist to disk
     * @throws AiderException if the file cannot be written
     */
    public void save(ArrayList<Task> tasks) throws AiderException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new AiderException("Could not create the data directory: " + parent.getPath());
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + System.lineSeparator());
            }
        } catch (IOException exception) {
            throw new AiderException("Could not save the data file: " + exception.getMessage());
        }
    }

    /**
     * Parses a single line from the data file into a Task.
     *
     * @param line the line to parse
     * @return the parsed task
     * @throws AiderException if the line does not match a known task format
     */
    private Task parseLine(String line) throws AiderException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new AiderException("Could not parse saved task: " + line);
        }

        String type = parts[0];
        boolean done = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new AiderException("Could not parse saved deadline: " + line);
            }
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts.length < 5) {
                throw new AiderException("Could not parse saved event: " + line);
            }
            task = new Event(description, parts[3], parts[4]);
            break;
        default:
            throw new AiderException("Unknown task type in save file: " + type);
        }

        if (done) {
            task.markAsDone();
        }
        return task;
    }
}
