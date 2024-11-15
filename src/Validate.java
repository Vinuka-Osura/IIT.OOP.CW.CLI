import java.util.InputMismatchException;
import java.util.Scanner;

public class Validate {
    private static Scanner scanner;
    public Validate() {
        scanner = new Scanner(System.in);
    }

    public static Configuration setupConfiguration() {
        int totalTickets = promptForInt("Enter total number of tickets: ");
        int ticketReleaseRate = promptForInt("Enter ticket release rate (tickets per second): ");
        int customerRetrievalRate = promptForInt("Enter customer retrieval rate (tickets per second): ");
        int maxTicketCapacity = promptForInt("Enter maximum ticket capacity: ");

        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }

    static int promptForInt(String message) {
        int value;
        while (true) {
            try {
                System.out.print(message);
                value = scanner.nextInt();
                if (value <= 0) {
                    System.out.println("Please enter a positive number.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
        return value;
    }

}
