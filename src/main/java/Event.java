/**
 * A task that starts and ends at specified dates or times.
 */
public class Event extends Task {
    /** The date or time when the event starts. */
    private final String from;

    /** The date or time when the event ends. */
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the event start date or time
     * @param to the event end date or time
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        assert from != null : "Event start time must not be null";
        assert to != null : "Event end time must not be null";
        this.from = from;
        this.to = to;
    }

    @Override
    protected String getTaskDetails() {
        return description + " (from: " + from + " to: " + to + ")";
    }

    @Override
    protected String getScheduleText() {
        return from + " to " + to;
    }
}
