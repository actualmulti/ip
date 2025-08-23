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
        String[] taskList = new String[100];
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
                for (int i = 0; i < index; i++) {
                    System.out.printf("%d. %s\n", i, taskList[i]);
                }
                System.out.println("____________________________________________________________");
                continue;

            }

            System.out.println("____________________________________________________________");
            System.out.printf("added: %s\n", input);
            taskList[index] = input;
            index++;
            System.out.println("____________________________________________________________");
        }


    }
}
