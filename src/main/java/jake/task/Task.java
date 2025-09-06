package jake.task;

/**
 * Abstract base class representing a task in the Jake task management system.
 * Provides common functionality for all task types including completion status tracking,
 * name management, and basic operations like marking done/undone.
 *
 * Subclasses must implement the toFileString() method to define their file storage format.
 */
public abstract class Task {
    protected final String name;
    protected boolean done;

    /**
            * Creates a new task with the specified name.
     * The task is initially marked as not done.
            *
            * @param name the name or description of the task
     */
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
