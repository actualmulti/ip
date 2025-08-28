import java.util.Scanner;
import java.util.ArrayList;

public class Jake {
    public static void main(String[] args) {
        // Initialize the scanner
        Scanner scanner = new Scanner(System.in);

        // Storage system
        Storage storage = new Storage("./data/jake.txt");
        ArrayList<Task> taskList = storage.load();
        int index = taskList.size();

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
                    System.out.printf("%d. %s\n", i + 1, taskList.get(i).toString());
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark") && Integer.parseInt(input.split(" ")[1]) <= index) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
                Task task = taskList.get(taskNumber);
                task.markDone();
                storage.save(taskList);
                System.out.println("____________________________________________________________");
                System.out.println("The following task has been marked as done:");
                System.out.println(task.toString());
                System.out.println("____________________________________________________________");
            } else if(input.startsWith("unmark") && Integer.parseInt(input.split(" ")[1]) <= index) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
                Task task = taskList.get(taskNumber);
                task.unmarkDone();
                storage.save(taskList);
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
                    taskList.add(new Todo(name));
                    storage.save(taskList);
//                    taskList.set(index, new Todo(name));
                    System.out.println("______________________________________________________________");
                    System.out.println("Todo task has been added:");
                    System.out.println(taskList.get(index).toString());
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
                    taskList.add(new DeadlineTask(name, deadline));
                    storage.save(taskList);
//                    taskList.set(index, new DeadlineTask(name, deadline));
                    System.out.println("______________________________________________________________");
                    System.out.println("Deadline task has been added:");
                    System.out.println(taskList.get(index).toString());
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
                    taskList.add(new EventTask(name, startDate, endDate));
                    storage.save(taskList);
//                    taskList.set(index, new EventTask(name, startDate, endDate));
                    System.out.println("______________________________________________________________");
                    System.out.println("Event task has been added:");
                    System.out.println(taskList.get(index).toString());
                    System.out.printf("Now you have %d tasks in the list.\n", index + 1);
                    System.out.println("______________________________________________________________");
                    index++;
                } catch (JakeException e) {
                    System.out.println("______________________________________________________________");
                    System.out.println(e.getMessage());
                    System.out.println("______________________________________________________________");
                }
            } else if (input.startsWith("delete")) {
                try {
                    int positionIndex = Integer.parseInt(input.split(" ")[1]);
                    if (positionIndex <= 0 || positionIndex > index) {
                        throw new JakeException("Invalid entry!");
                    }
                    System.out.println("______________________________________________________________");
                    System.out.println("The following task has been removed:");
                    System.out.println(taskList.get(positionIndex - 1).toString());
                    System.out.printf("Now you have %d tasks in the list.\n", index - 1);
                    System.out.println("______________________________________________________________");
                    taskList.remove(positionIndex - 1);
                    index--;
                    storage.save(taskList);
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

