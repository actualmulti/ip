package jake.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jake.JakeException;

/**
 * Represents a task that occurs during a specific time period with start and end times.
 * Both start and end dates are parsed from ISO format (yyyy-MM-ddTHH:mm:ss) and
 * displayed in a more readable format (MMM dd yyyy HH:mm:ss).
 *
 * Example: "team meeting (from: Dec 25 2023 10:00:00 to: Dec 25 2023 11:30:00)"
 */
public class EventTask extends Task {
    private String startDate;
    private String endDate;

    /**
     * Creates a new event task with the specified name, start time, and end time.
     *
     * @param name the description of the event
     * @param startDate the start time in ISO format (yyyy-MM-ddTHH:mm:ss)
     * @param endDate the end time in ISO format (yyyy-MM-ddTHH:mm:ss)
     * @throws JakeException if either date format is invalid
     */
    public EventTask(String name, String startDate, String endDate) {
        super(name);
        this.startDate = parseDate(startDate);
        this.endDate = parseDate(endDate);
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
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
        return "E | " + (done ? "1" : "0") + " | " + name + " | " + startDate + " | " + endDate;
    }
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), startDate, endDate);
    }
}
