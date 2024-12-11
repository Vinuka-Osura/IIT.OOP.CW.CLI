import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketSystem {
    private final Validate validate;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicBoolean paused = new AtomicBoolean(false);
    private final List<Thread> customerThreads = new ArrayList<>();
    private final List<Thread> vendorThreads = new ArrayList<>();
    private Thread listenerThread;

    public TicketSystem() {
        this.validate = new Validate();
    }

    public Configuration setupConfiguration() {
        int totalTickets = Validate.promptForInt("Enter total number of tickets: ");
        int ticketReleaseRate = Validate.promptForInt("Enter ticket release rate (tickets releases per time): ");
        int customerRetrievalRate = Validate.promptForInt("Enter customer retrieval rate (tickets retrieval per time): ");
        int maxTicketCapacity = Validate.promptForInt("Enter maximum ticket capacity of ticket pool: ");
        int numberOfVendors = Validate.promptForInt("Enter the number of vendor threads: ");
        int numberOfCustomers = Validate.promptForInt("Enter the number of customer threads: ");

        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, numberOfVendors, numberOfCustomers);
    }

    public void startOperations(Configuration config) {
        if (running.get()) {
            Logger.log("ticket manage operations are already running!");
            return;
        }

        running.set(true);
        paused.set(false);
        Logger.log("Starting ticket manage operations...");

        TicketPool ticketPool = new TicketPool(config.getTotalTickets(), config.getMaxTicketCapacity());

        for (int i = 0; i < config.getNumberOfVendors(); i++) {
            Vendor vendor = new Vendor(ticketPool, config.getTicketReleaseRate(), this);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }


        for (int i = 0; i < config.getNumberOfCustomers(); i++) {
            Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate(), this);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }
    }

    public void stopOperations() {
        if (!running.get()) {
            System.out.println("Ticket operations are not running safe to exit..........");
            return;
        }

        running.set(false);
        paused.set(false);
        Logger.log("Stopping ticket handling operations.........");

        for (Thread vendorThread : vendorThreads) {
            vendorThread.interrupt();
        }
        vendorThreads.clear();

        for (Thread customerThread : customerThreads) {
            customerThread.interrupt();
        }
        customerThreads.clear();

        if (listenerThread != null && listenerThread.isAlive()) {
            listenerThread.interrupt();
        }
    }

    public boolean isPaused() {
        return paused.get();
    }

    public void listenForCommands(Configuration config) {
        System.out.println("Type 'start' to begin operations, 'stop' to end operations, or 'exit' to quit.");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    startOperations(config);
                    break;
                case "stop":
                    stopOperations();
                    break;
                case "exit":
                    stopOperations();
                    Logger.log("Exiting system. Goodbye Have a nice Day!");
                    Logger.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid command. Please type a valid command ('start', 'stop', or 'exit').");
            }
        }
    }
}
