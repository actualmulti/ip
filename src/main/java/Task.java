public class Task {
    private final String name;
    private boolean done;

    public Task(String name) {
        this.name = name;
        this.done = false;
    }

    public boolean isDone() {
        return done;
    }

    public void markDone() {
        this.done = true;
    }

    public void unmarkDone() {
        this.done = false;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        String marker;
        if (this.isDone()) {
            marker = "X";
        } else {
            marker = " ";
        }
        return String.format("[%s] %s", marker, this.name);
    }
}
