package jake;

import jake.task.Task;

import java.util.ArrayList;

/**
 * Manages a list of tasks with operations to add, delete, mark, and access tasks.
 * Provides a wrapper around ArrayList with additional validation and task-specific operations.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with given list of tasks
     * @param tasks the initial list of tasks to be managed
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the task list
     * @param task the task to be added to the list
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index the zero-based index of the task to be removed
     * @throws JakeException if the index is invalid or out of bounds
     */
    public void delete(int index) throws JakeException {
        if (index < 0 || index >= tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        tasks.remove(index);
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index the zero-based index of the task to retrieve
     * @return the task at the specified index
     * @throws JakeException if the index is invalid or out of bounds
     */
    public Task get(int index) throws JakeException {
        if (index < 0 || index >= tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        return tasks.get(index);
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index the zero-based index of the task to mark as done
     * @throws JakeException if the index is invalid or out of bounds
     */
    public void markTask(int index) throws JakeException {
        get(index).markDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index the zero-based index of the task to unmark
     * @throws JakeException if the index is invalid or out of bounds
     */
    public void unmarkTask(int index) throws JakeException {
        get(index).unmarkDone();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying ArrayList of tasks.
     * Used primarily for storage operations.
     *
     * @return the ArrayList containing all tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
