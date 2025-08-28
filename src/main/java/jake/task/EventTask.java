package jake.task;

import jake.JakeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventTask extends Task {
    private String startDate;
    private String endDate;

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
