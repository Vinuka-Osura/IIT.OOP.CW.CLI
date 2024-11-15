import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketSystem {
    private final Validate validate;
    private AtomicBoolean running = new AtomicBoolean(false);  // Atomic flag to control running state
    private AtomicBoolean paused = new AtomicBoolean(false);
    private Thread vendorThread;
    private PriorityBlockingQueue<Customer> customerQueue;
    private Vendor vendor;
    private Thread listenerThread;


    public TicketSystem() {
        this.validate = new Validate();
        this.customerQueue = new PriorityBlockingQueue<>();
    }

    // Method to set up configuration by using Validate for inputs
    public Configuration setupConfiguration() {
        int totalTickets = validate.promptForInt("Enter total number of tickets: ");
        int ticketReleaseRate = validate.promptForInt("Enter ticket release rate (tickets per second): ");
        int customerRetrievalRate = validate.promptForInt("Enter customer retrieval rate (tickets per second): ");
        int maxTicketCapacity = validate.promptForInt("Enter maximum ticket capacity: ");

        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }

    // Start ticket handling operation
    public void startOperations(Configuration config) {
        if (running.get()) {
            System.out.println("Operations are already running!");
            return;
        }

        running.set(true);
        paused.set(false);
        System.out.println("Starting ticket handling operations...");
        startStopKeyListener();

        // Initialize TicketPool
        TicketPool ticketPool = new TicketPool(config.getTotalTickets(), config.getMaxTicketCapacity());

        //start vendor/customer threads
        vendor = new Vendor(ticketPool, config.getTicketReleaseRate(), this);
        vendorThread = new Thread(vendor);
        vendorThread.start();

        addCustomer(ticketPool, config.getCustomerRetrievalRate(), 1); // Regular customer
        addCustomer(ticketPool, config.getCustomerRetrievalRate(), 2); // VIP customer
        addCustomer(ticketPool, config.getCustomerRetrievalRate(), 3); // Super VIP customer

        processCustomers();
    }

    public void addCustomer(TicketPool ticketPool, int retrievalRate, int priority) {
        Customer customer = new Customer(ticketPool, retrievalRate, priority, this);
        customerQueue.add(customer);
        Thread customerThread = new Thread(customer);
        customerThread.start();
    }

    public void processCustomers() {
        while (running.get()) {
            if (paused.get()) { // Skip processing if paused
                try {
                    Thread.sleep(100); // Small delay to avoid busy-waiting
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            try {
                Customer customer = customerQueue.take(); // Process customer with the highest priority
                new Thread(customer).start();
            } catch (InterruptedException e) {
                System.out.println("Customer processing interrupted.");
                break;
            }
        }
    }

    public void startStopKeyListener() {
        if (listenerThread != null && listenerThread.isAlive()) {
            return; // Avoid multiple listeners
        }

        listenerThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (!running.get()) {  // Only prompt if the system is running
                    try {
                        Thread.sleep(100);  // Small delay to avoid overloading CPU
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue;
                }

                System.out.println("Press 's' and hit Enter to stop the system.");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("s")) {

                    paused.set(true); // Set pause flag to pause all threads
                    System.out.print("Do you want to stop the operations? (yes/no): ");
                    String response = scanner.nextLine().trim().toLowerCase();

                    if (response.equals("yes")) {
                        stopOperations();
                        break;
                    } else {
                        System.out.println("Continuing operations...");
                        paused.set(false);
                    }
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    // Stop ticket handling operation
    public void stopOperations() {
        if (!running.get()) {
            System.out.println("Operations are not running!");
            return;
        }

        running.set(false);
        paused.set(false);
        System.out.println("Stopping ticket handling operations...");

        // Stop threads by setting flags and interrupting
        if (vendor != null) {
            vendor.stop();
            vendorThread.interrupt();
        }

        for (Customer customer : customerQueue) {
            customer.stop();
        }

        customerQueue.clear();// clear queue (customer)

//        vendor = null; // Reset vendor to null for reinitialization
//        paused.set(false);

        if (listenerThread != null && listenerThread.isAlive()) {
            listenerThread.interrupt();
        }
    }

    public boolean isPaused() {
        return paused.get();
    }

    // listen for commands
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
                    System.out.println("Exiting system. Goodbye!");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid command. Please type 'start', 'stop', or 'exit'.");
            }
        }
    }
}
