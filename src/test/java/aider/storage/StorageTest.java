package aider.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import aider.model.Deadline;
import aider.model.Event;
import aider.model.Task;
import aider.model.Todo;

/** Tests persistence of the supported task types. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void saveAndLoadRoundTripsTasks() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("duke.txt").toString());
        Todo todo = new Todo("read book");
        todo.markAsDone();
        ArrayList<Task> tasks = new ArrayList<>(List.of(
                todo,
                new Deadline("submit report", LocalDateTime.of(2026, 9, 1, 9, 0)),
                new Event("conference", LocalDateTime.of(2026, 9, 1, 10, 0),
                        LocalDateTime.of(2026, 9, 1, 12, 0))));

        storage.save(tasks);
        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(3, loadedTasks.size());
        assertEquals("[T][X] read book", loadedTasks.get(0).toString());
        assertEquals(tasks.get(1).toString(), loadedTasks.get(1).toString());
        assertEquals(tasks.get(2).toString(), loadedTasks.get(2).toString());
    }
}
