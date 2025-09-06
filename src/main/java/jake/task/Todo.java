package jake.task;

/**
 * Represents a simple todo task without any date constraints.
 * A todo task only has a name and completion status.
 *
 * Example: "buy groceries", "read book"
 */
public class Todo extends Task {
    public Todo(String name) {
        super(name);
    }

    @Override
    public String toFileString() {
        return "T | " + (done ? "1" : "0") + " | " + name;
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
