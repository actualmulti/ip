public class Todo extends Task{
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
