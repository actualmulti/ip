package jake.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jake.JakeException;

/**
 * Represents a task that has a deadline by which it must be completed.
 * The deadline is parsed from ISO format (yyyy-MM-ddTHH:mm:ss) and
 * displayed in a more readable format (MMM dd yyyy HH:mm:ss).
 *
 * Example: "submit assignment (by: Dec 25 2023 23:59:59)"
 */
public class DeadlineTask extends Task {
    private String deadlineDate;

    /**
     * Creates a new deadline task with the specified name and deadline.
     *
     * @param name the description of the task
     * @param deadlineDate the deadline in ISO format (yyyy-MM-ddTHH:mm:ss)
     * @throws JakeException if the deadline date format is invalid
     */
    public DeadlineTask(String name, String deadlineDate) {
        super(name);
        this.deadlineDate = parseDate(deadlineDate);
    }

    public String getType() {
        return "D";
    }
    public String getDeadlineDate() {
        return this.deadlineDate;
    }

    private String parseDate(String date) {
        try {
            LocalDateTime datetime = LocalDateTime.parse(date);
            return datetime.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss"));
        } catch (Exception e) {
            throw new JakeException("Invalid datetime input! Input in yyyy-mm-ddTHH:mm:ss format");
        }
    }

    @Override
    public String toFileString() {
        String tagsString = String.join(",", this.tags);
        return "D | " + (isDone ? "1" : "0") + " | " + name + " | " + deadlineDate + " | " + tagsString;
    }
    @Override
    public String toString() {
        String tagsDisplay = getTagsDisplay();
        if (tagsDisplay.isEmpty()) {
            return String.format("[D]%s (by: %s)", super.toString(), deadlineDate);
        } else {
            return String.format("[D]%s (by: %s) | %s", super.toString(), deadlineDate, tagsDisplay);
        }
    }
}
