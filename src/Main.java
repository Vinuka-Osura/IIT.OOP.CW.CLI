//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // ---------------------------------------------------------
        System.out.print("Hello and welcome!");
        // ----------------------------------------------------------

        TicketSystem ticketSystem = new TicketSystem();
        Configuration config = ticketSystem.setupConfiguration();

        System.out.println("Configuration setup complete:");
        System.out.println("Total Tickets: " + config.getTotalTickets());
        System.out.println("Ticket Release Rate: " + config.getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());
        System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());

        ticketSystem.listenForCommands(config);
    }
}
