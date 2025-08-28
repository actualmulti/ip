package jake.ui;

import jake.*;
import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;

import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

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

    public void showGoodbye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with empty task list.");
    }

    public void showTaskAdded(Task task, int totalTasks) {
        showLine();
        System.out.println(getTaskTypeString(task) + " task has been added:");
        System.out.println(task.toString());
        System.out.printf("Now you have %d tasks in the list.\n", totalTasks);
        showLine();
    }

    public void showTaskMarked(Task task) {
        showLine();
        System.out.println("The following task has been marked as done:");
        System.out.println(task.toString());
        showLine();
    }

    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println("The following task has been unmarked:");
        System.out.println(task.toString());
        showLine();
    }

    public void showTaskList(TaskList tasks) {
        showLine();
        System.out.println("List of tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, tasks.get(i).toString());
        }
        showLine();
    }

    public void showTaskDeleted(Task task, int totalTasks) {
        showLine();
        System.out.println("The following task has been removed:");
        System.out.println(task.toString());
        System.out.printf("Now you have %d tasks in the list.\n", totalTasks);
        showLine();
    }

    public void showInvalidCommand() {
        showLine();
        System.out.println("Invalid task!!! Try another one");
        showLine();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

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
