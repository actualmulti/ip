package jake;


import java.util.ArrayList;

import jake.task.Task;
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
        assert task != null : "task should not be null";
        assert tasks != null : "tasklist should be initialized";

        int sizeBefore = tasks.size();
        tasks.add(task);

        assert tasks.size() == sizeBefore + 1 : "list size should increase by 1 after adding";
        assert tasks.contains(task) : "task should be present in list after adding";
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index the zero-based index of the task to be removed
     * @throws JakeException if the index is invalid or out of bounds
     */
    public void delete(int index) throws JakeException {
        assert tasks != null : "tasklist should be initialized";
        assert index >= 0 : "index should be non-negative";

        if (index >= tasks.size()) {
            throw new JakeException("Invalid task number!");
        }

        int sizeBefore = tasks.size();
        Task removedTask = tasks.get(index);
        tasks.remove(index);

        assert tasks.size() == sizeBefore - 1 : "list size should increase by 1";
        assert tasks.indexOf(removedTask) != index
                || !tasks.contains(removedTask) : "task should be removed from speicifed index";
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
        assert index >= 0 : "index should be non-negative";

        get(index).markDone();
        assert get(index).isDone() : "task should be marked as done after markDone()";
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index the zero-based index of the task to unmark
     * @throws JakeException if the index is invalid or out of bounds
     */
    public void unmarkTask(int index) throws JakeException {
        assert index >= 0 : "index should be non-negative";

        get(index).unmarkDone();
        assert !get(index).isDone() : "task should not be marked as done after unmarkDone()";
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

    /**
     * Returns the list of all tasks that contain the string input.
     * @param name the string to be found in the list of tasks.
     * @return the TaskList containing all tasks that contain the input string.
     */
    public TaskList findTasks(String name) {
        ArrayList<Task> out = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getName().contains(name)) {
                out.add(task);
            }
        }
        return new TaskList(out);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
