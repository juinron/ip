import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests date and time matching for the B-ViewSchedules extension. */
public class TaskScheduleTest {
    /** A deadline matches its date or time without depending on letter case. */
    @Test
    public void deadlineMatchesDateCaseInsensitively() {
        Deadline deadline = new Deadline("submit report", "Friday");

        assertTrue(deadline.isScheduledOn("friday"));
        assertFalse(deadline.isScheduledOn("Monday"));
    }

    /** An event matches either its start or end time. */
    @Test
    public void eventMatchesStartOrEndTime() {
        Event event = new Event("project meeting", "Friday 2pm", "Friday 4pm");

        assertTrue(event.isScheduledOn("2PM"));
        assertTrue(event.isScheduledOn("4pm"));
    }

    /** A todo without a date or time is not part of any schedule. */
    @Test
    public void todoIsNotScheduled() {
        Todo todo = new Todo("read book");

        assertFalse(todo.isScheduledOn("Friday"));
    }
}
