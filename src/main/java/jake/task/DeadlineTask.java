package jake.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jake.JakeException;

public class DeadlineTask extends Task {
    private String deadlineDate;

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
        return "D | " + (done ? "1" : "0") + " | " + name + " | " + deadlineDate;
    }
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), deadlineDate);
    }
}
