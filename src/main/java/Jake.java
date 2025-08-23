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
                    String marked;
                    if (taskList[i].isDone()) {
                        marked = "X";
                    } else {
                        marked = " ";
                    }
                    System.out.printf("%d. [%s] %s\n", i, marked, taskList[i].getName());
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark") && Integer.parseInt(input.split(" ")[1]) < index) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]);
                Task task = taskList[taskNumber];
                task.markDone();
                System.out.println("____________________________________________________________");
                System.out.println("The following task has been marked as done:");
                System.out.printf("[X] %s\n", task.getName());
                System.out.println("____________________________________________________________");
            } else if(input.startsWith("unmark") && Integer.parseInt(input.split(" ")[1]) < index) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]);
                Task task = taskList[taskNumber];
                task.unmarkDone();
                System.out.println("____________________________________________________________");
                System.out.println("The following task has been unmarked:");
                System.out.printf("[ ] %s\n", task.getName());
                System.out.println("______________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.printf("added: %s\n", input);
                taskList[index] = new Task(input);
                index++;
                System.out.println("____________________________________________________________");
            }
        }


    }
}

