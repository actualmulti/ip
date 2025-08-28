import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void delete(int index) throws JakeException {
        if (index < 0 || index >= tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        tasks.remove(index);
    }

    public Task get(int index) throws JakeException {
        if (index < 0 || index >= tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        return tasks.get(index);
    }

    public void markTask(int index) throws JakeException {
        get(index).markDone();
    }

    public void unmarkTask(int index) throws JakeException {
        get(index).unmarkDone();
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
