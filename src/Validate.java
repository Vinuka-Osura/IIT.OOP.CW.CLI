import java.util.InputMismatchException;
import java.util.Scanner;

public class Validate {
    private static Scanner scanner;
    public Validate() {
        scanner = new Scanner(System.in);
    }

    static int promptForInt(String message) {
        int value;
        while (true) {
            try {
                System.out.print(message);
                value = scanner.nextInt();
                if (value <= 0) {
                    Logger.log(value + "is not positive please enter a positive number.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                Logger.log("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
        return value;
    }

}
