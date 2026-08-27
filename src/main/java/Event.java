import java.time.LocalDateTime;

/**
 * A task that starts and ends at specified dates or times.
 */
public class Event extends Task {
    /** The date or time when the event starts. */
    private final LocalDateTime from;

    /** The date or time when the event ends. */
    private final LocalDateTime to;

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the event start date or time
     * @param to the event end date or time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start date and time.
     *
     * @return the event start date and time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event end date and time.
     *
     * @return the event end date and time
     */
    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    protected String getTaskDetails() {
        return description + " (from: " + DateTimeParser.format(from)
                + " to: " + DateTimeParser.format(to) + ")";
    }

    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | "
                + DateTimeParser.toStorageString(from) + " | "
                + DateTimeParser.toStorageString(to);
    }
}
