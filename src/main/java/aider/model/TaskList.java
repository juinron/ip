package aider.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import aider.AiderException;

/** Owns the tasks and operations performed on the task list. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from loaded tasks.
     *
     * @param tasks the tasks to include
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the number of tasks. */
    public int size() {
        return tasks.size();
    }

    /** Returns whether this task list contains no tasks. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns the task at a zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns a task at a zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns a copy suitable for persistence. */
    public ArrayList<Task> asList() {
        return new ArrayList<>(tasks);
    }

    /** Returns the zero-based index represented by a user task number. */
    public int indexOf(String command, String commandName) throws AiderException {
        String numberText = command.substring(commandName.length()).trim();
        if (numberText.isEmpty()) {
            throw new AiderException("The " + commandName + " command needs a task number.");
        }
        final int number;
        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new AiderException("The " + commandName
                    + " command needs a whole-number task number.");
        }
        int index = number - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new AiderException("That task number does not exist.");
        }
        return index;
    }

    /** Marks a task complete. */
    public Task mark(String command) throws AiderException {
        Task task = get(indexOf(command, "mark"));
        task.markAsDone();
        return task;
    }

    /** Marks a task incomplete. */
    public Task unmark(String command) throws AiderException {
        Task task = get(indexOf(command, "unmark"));
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns deadlines and events occurring on a date.
     *
     * @param date the date to search
     * @return matching tasks in list order
     */
    public ArrayList<Task> occurringOn(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline
                    && ((Deadline) task).getBy().toLocalDate().equals(date)) {
                matches.add(task);
            } else if (task instanceof Event) {
                Event event = (Event) task;
                if (event.getFrom().isBefore(end) && !event.getTo().isBefore(start)) {
                    matches.add(task);
                }
            }
        }
        return matches;
    }
}
