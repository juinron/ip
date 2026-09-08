import java.util.Locale;

/**
 * Represents a task entered by the user.
 */
public class Task {
    /** The text describing the task. */
    protected String description;

    /** Whether the task has been completed. */
    protected boolean isDone;

    /** The type of task represented by this object. */
    private final TaskType type;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this(description, TaskType.TODO);
    }

    /**
     * Creates an incomplete task with the given description and type.
     *
     * @param description the text describing the task
     * @param type the type of task represented by this object
     */
    protected Task(String description, TaskType type) {
        assert description != null : "Task description must not be null";
        this.description = description;
        this.isDone = false;
        this.type = type;
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
        return type.getIcon();
    }

    /**
     * Checks whether this task is scheduled on the supplied date or time.
     *
     * @param dateOrTime the date or time to search for
     * @return whether this task has a matching schedule entry
     */
    public boolean isScheduledOn(String dateOrTime) {
        String scheduleText = getScheduleText().toLowerCase(Locale.ROOT);
        return !scheduleText.isEmpty()
                && scheduleText.contains(dateOrTime.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns the free-form scheduling text associated with this task.
     *
     * @return the scheduling text, or an empty string when unscheduled
     */
    protected String getScheduleText() {
        return "";
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
