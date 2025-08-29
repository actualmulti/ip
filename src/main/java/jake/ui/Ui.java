package jake.ui;

import jake.*;
import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;

import java.util.Scanner;

/**
 * Handles all user interface interactions for the Jake application.
 * Manages input from users and output display including welcome messages,
 * task information, error messages, and formatted lists.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Creates a new Ui instance and initializes the scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message with the Jake logo and greeting.
     */
    public void showWelcome() {
        String logo =
                "     _   _    _  _______ \n"
                        + "    | | / \\  | |/ / ____|\n"
                        + " _  | |/ _ \\ | ' /|  _|  \n"
                        + "| |_| / ___ \\| . \\| |___ \n"
                        + " \\___/_/   \\_\\_|\\_\\_____|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("What can I do for you today?");
        showLine();
    }

    /**
     * Displays the goodbye message when the user exits the application.
     */
    public void showGoodbye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays a horizontal line separator for formatting output.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays an error message surrounded by horizontal lines.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Displays an error message when tasks cannot be loaded from the storage file.
     */
    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with empty task list.");
    }

    /**
     * Displays a confirmation message when a task is successfully added.
     *
     * @param task the task that was added
     * @param totalTasks the total number of tasks in the list after adding
     */
    public void showTaskAdded(Task task, int totalTasks) {
        showLine();
        System.out.println(getTaskTypeString(task) + " task has been added:");
        System.out.println(task.toString());
        System.out.printf("Now you have %d tasks in the list.\n", totalTasks);
        showLine();
    }

    /**
     * Displays a confirmation message when a task is marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showTaskMarked(Task task) {
        showLine();
        System.out.println("The following task has been marked as done:");
        System.out.println(task.toString());
        showLine();
    }

    /**
     * Displays a confirmation message when a task is unmarked (marked as not done).
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println("The following task has been unmarked:");
        System.out.println(task.toString());
        showLine();
    }

    /**
     * Displays the complete list of tasks with numbering.
     *
     * @param tasks the TaskList containing all tasks to display
     */
    public void showTaskList(TaskList tasks) {
        showLine();
        System.out.println("List of tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, tasks.get(i).toString());
        }
        showLine();
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param task the task that was deleted
     * @param totalTasks the total number of tasks remaining in the list
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        showLine();
        System.out.println("The following task has been removed:");
        System.out.println(task.toString());
        System.out.printf("Now you have %d tasks in the list.\n", totalTasks);
        showLine();
    }

    /**
     * Displays an error message for invalid or unrecognized commands.
     */
    public void showInvalidCommand() {
        showLine();
        System.out.println("Invalid task!!! Try another one");
        showLine();
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the user's input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Determines the display string for different task types.
     *
     * @param task the task whose type string is needed
     * @return a string representing the task type for display purposes
     */
    private String getTaskTypeString(Task task) {
        if (task instanceof Todo) {
            return "Todo";
        } else if (task instanceof DeadlineTask) {
            return "Deadline";
        } else if (task instanceof EventTask) {
            return "Event";
        }
        return "jake.task.Task";
    }
}
