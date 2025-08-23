import java.sql.SQLOutput;
import java.util.Scanner;

public class Jake {
    public static void main(String[] args) {
        // Initialize the scanner
        Scanner scanner = new Scanner(System.in);

        // JAKE logo
        String logo =
                "     _   _    _  _______ \n"
                        + "    | | / \\  | |/ / ____|\n"
                        + " _  | |/ _ \\ | ' /|  _|  \n"
                        + "| |_| / ___ \\| . \\| |___ \n"
                        + " \\___/_/   \\_\\_|\\_\\_____|\n";

//        System.out.println("Hello my name is Jake.");
        System.out.println("Hello from\n" + logo);
        System.out.println("What can I do for you today?");
        System.out.println("____________________________________________________________");

        // Chat logic
        // Task list
        Task[] taskList = new Task[100];

        int index = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                System.out.println("List of tasks:");
                for (int i = 0; i < index; i++) {
                    System.out.printf("%d. %s\n", i + 1, taskList[i].toString());
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark") && Integer.parseInt(input.split(" ")[1]) <= index) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
                Task task = taskList[taskNumber];
                task.markDone();
                System.out.println("____________________________________________________________");
                System.out.println("The following task has been marked as done:");
                System.out.println(task.toString());
                System.out.println("____________________________________________________________");
            } else if(input.startsWith("unmark") && Integer.parseInt(input.split(" ")[1]) <= index) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
                Task task = taskList[taskNumber];
                task.unmarkDone();
                System.out.println("____________________________________________________________");
                System.out.println("The following task has been unmarked:");
                System.out.println(task.toString());
                System.out.println("______________________________________________________________");
            } else if(input.startsWith("todo")) {
                try {
                    String name = input.substring(input.indexOf(" ") + 1);
                    if (name.isEmpty() || name.equals("todo")) {
                        throw new JakeException("Todo task must have a name!");
                    }
                    taskList[index] = new Todo(name);
                    System.out.println("______________________________________________________________");
                    System.out.println("Todo task has been added:");
                    System.out.println(taskList[index].toString());
                    System.out.printf("Now you have %d tasks in the list.\n", index + 1);
                    System.out.println("______________________________________________________________");
                    index++;
                } catch (JakeException e) {
                    System.out.println("______________________________________________________________");
                    System.out.println(e.getMessage());
                    System.out.println("______________________________________________________________");
                }
            } else if(input.startsWith("deadline")) {
                try {
                    int beginIndex = input.indexOf(" ") + 1;
                    int endIndex = input.indexOf("/");
                    if (beginIndex > endIndex) {
                        throw new JakeException("Deadline task must have a valid name and/or date!");
                    }
                    String name = input.substring(beginIndex, endIndex - 1);
                    String deadline = input.substring(endIndex + 1);
                    taskList[index] = new DeadlineTask(name, deadline);
                    System.out.println("______________________________________________________________");
                    System.out.println("Deadline task has been added:");
                    System.out.println(taskList[index].toString());
                    System.out.printf("Now you have %d tasks in the list.\n", index + 1);
                    System.out.println("______________________________________________________________");
                    index++;
                } catch (JakeException e) {
                    System.out.println("______________________________________________________________");
                    System.out.println(e.getMessage());
                    System.out.println("______________________________________________________________");
                }
            } else if(input.startsWith("event")) {
                try {
                    int beginIndex = input.indexOf(" ") + 1;
                    int endIndex = input.indexOf("/");
                    if (beginIndex >= endIndex) {
                        throw new JakeException("Event task must have a valid name and/or date!");
                    }
                    String name = input.substring(beginIndex, endIndex - 1);
                    String dates = input.substring(endIndex + 1);
                    int dateIndex = dates.indexOf("/");
                    if (dateIndex < 0) {
                        throw new JakeException("Event task must have a valid name and/or date!");
                    }
                    String startDate = dates.substring(0, dateIndex - 1);
                    String endDate = dates.substring(dateIndex + 1);
                    taskList[index] = new EventTask(name, startDate, endDate);
                    System.out.println("______________________________________________________________");
                    System.out.println("Event task has been added:");
                    System.out.println(taskList[index].toString());
                    System.out.printf("Now you have %d tasks in the list.\n", index + 1);
                    System.out.println("______________________________________________________________");
                    index++;
                } catch (JakeException e) {
                    System.out.println("______________________________________________________________");
                    System.out.println(e.getMessage());
                    System.out.println("______________________________________________________________");
                }
            } else {
                System.out.println("______________________________________________________________");
                System.out.println("Invalid task!!! Try another one");
                System.out.println("______________________________________________________________");
            }
        }
    }
}

