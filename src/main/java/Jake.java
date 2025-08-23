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
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            System.out.println("____________________________________________________________");
            System.out.println(input);
            System.out.println("____________________________________________________________");
        }


    }
}
