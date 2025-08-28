public abstract class Task {
    protected final String name;
    protected boolean done;

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

    public abstract String toFileString();

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
