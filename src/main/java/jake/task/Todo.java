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
        String tagsString = String.join(",", tags);
        return "T | " + (isDone ? "1" : "0") + " | " + name + " | " + tagsString;
    }

    @Override
    public String toString() {
        String tagsDisplay = getTagsDisplay();
        if (tagsDisplay.isEmpty()) {
            return String.format("[T]%s", super.toString());
        } else {
            return String.format("[T]%s | %s", super.toString(), tagsDisplay);
        }
    }
}
