package aider.model;

import java.time.LocalDateTime;

import aider.util.DateTimeParser;

/**
 * A task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    /** The date or time by which the task should be completed. */
    private final LocalDateTime by;

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the date or time by which the task should be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline date and time.
     *
     * @return the deadline date and time
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    protected String getTaskDetails() {
        return description + " (by: " + DateTimeParser.format(by) + ")";
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | "
                + DateTimeParser.toStorageString(by);
    }
}
