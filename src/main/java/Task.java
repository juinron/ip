/**
 * Represents a task entered by the user.
 */
public class Task {
    /** The text describing the task. */
    protected String description;

    /** Whether the task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the symbol used to display this task's status.
     *
     * @return {@code X} when done, or a space when not done
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the one-letter icon for this task type.
     *
     * @return the task type icon
     */
    public String getTypeIcon() {
        return "T";
    }

    /**
     * Returns the description and any type-specific details.
     *
     * @return the display details for this task
     */
    protected String getTaskDetails() {
        return description;
    }

    /**
     * Returns the task in the format used by the chatbot.
     *
     * @return the formatted task
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + getTaskDetails();
    }
}
