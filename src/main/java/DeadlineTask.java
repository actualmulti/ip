public class DeadlineTask extends Task {
    private String deadlineDate;

    public DeadlineTask(String name, String deadlineDate) {
        super(name);
        this.deadlineDate = deadlineDate;
    }

    public String getType() {
        return "D";
    }
    public String getDeadlineDate() {
        return this.deadlineDate;
    }

    @Override
    public String toFileString() {
        return "D | " + (done ? "1" : "0") +  " | " + name + " | " + deadlineDate;
    }
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), deadlineDate);
    }
}
