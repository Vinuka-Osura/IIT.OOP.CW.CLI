//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Logger.log("Hello and welcome To the Real Time Ticketing System.......................");
        System.out.println();
        System.out.println("To Start the System please enter the System Configuration values");
        System.out.println();

        TicketSystem ticketSystem = new TicketSystem();
        Configuration config = ticketSystem.setupConfiguration();

        Logger.log("----------------------------------------------------------------");
        Logger.log("Configuration setup complete:");
        Logger.log("----------------------------------------------------------------");
        Logger.log("Total Tickets: " + config.getTotalTickets());
        Logger.log("Ticket Release Rate: " + config.getTicketReleaseRate());
        Logger.log("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());
        Logger.log("Max Ticket Capacity: " + config.getMaxTicketCapacity());
        Logger.log("----------------------------------------------------------------");
        Logger.log("Vendor Count: " + config.getNumberOfVendors());
        Logger.log("Customer Count: " + config.getNumberOfCustomers());
        Logger.log("----------------------------------------------------------------");
        System.out.println();
        System.out.println("Let's Start The System.................");
        System.out.println();

        ticketSystem.listenForCommands(config);
    }
}
