package aider.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/** Tests task-list operations and date filtering. */
class TaskListTest {
    @Test
    void markAndUnmarkUpdateTaskStatus() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        tasks.mark("mark 1");
        assertEquals("[T][X] read book", tasks.get(0).toString());

        tasks.unmark("unmark 1");
        assertEquals("[T][ ] read book", tasks.get(0).toString());
    }

    @Test
    void occurringOnReturnsDeadlinesAndOverlappingEvents() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("submit report", dateTime(2026, 9, 1, 9, 0)));
        tasks.add(new Event("conference", dateTime(2026, 8, 31, 23, 0),
                dateTime(2026, 9, 1, 1, 0)));

        assertEquals(2, tasks.occurringOn(LocalDate.of(2026, 9, 1)).size());
    }

    private static java.time.LocalDateTime dateTime(int year, int month, int day, int hour,
            int minute) {
        return java.time.LocalDateTime.of(year, month, day, hour, minute);
    }
}
