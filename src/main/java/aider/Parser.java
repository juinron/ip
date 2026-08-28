package aider;

import java.time.LocalDate;
import java.time.LocalDateTime;

import aider.model.Deadline;
import aider.model.Event;
import aider.model.Task;
import aider.model.Todo;
import aider.util.DateTimeParser;

/** Interprets user commands and creates the requested task objects. */
public class Parser {
    /** Creates a task from a todo, deadline, or event command. */
    public Task parseTask(String command) throws AiderException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new AiderException("A todo needs a description, for example: todo read book.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String details = command.substring("deadline".length()).trim();
            int byIndex = details.indexOf(" /by ");
            if (byIndex < 0) {
                throw new AiderException("A deadline must include a description and a /by date or time.");
            }
            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + " /by ".length()).trim();
            if (description.isEmpty() || by.isEmpty()) {
                throw new AiderException("A deadline needs a description and a date or time after /by.");
            }
            return new Deadline(description, DateTimeParser.parse(by));
        }

        if (command.equals("event") || command.startsWith("event ")) {
            String details = command.substring("event".length()).trim();
            int fromIndex = details.indexOf(" /from ");
            int toIndex = details.indexOf(" /to ");
            if (fromIndex < 0 || toIndex <= fromIndex) {
                throw new AiderException("An event needs a description, /from time, and /to time.");
            }
            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + " /from ".length(), toIndex).trim();
            String to = details.substring(toIndex + " /to ".length()).trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new AiderException("An event needs text after its description, /from, and /to markers.");
            }
            LocalDateTime fromDate = DateTimeParser.parse(from);
            LocalDateTime toDate = DateTimeParser.parse(to);
            if (toDate.isBefore(fromDate)) {
                throw new AiderException("An event cannot end before it starts.");
            }
            return new Event(description, fromDate, toDate);
        }

        throw new AiderException("I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or delete.");
    }

    /** Parses the date used by the on command. */
    public LocalDate parseDate(String command) throws AiderException {
        String dateText = command.substring("on".length()).trim();
        if (dateText.isEmpty()) {
            throw new AiderException("The on command needs a date, for example: on 2019-12-02.");
        }
        return DateTimeParser.parse(dateText).toLocalDate();
    }
}
