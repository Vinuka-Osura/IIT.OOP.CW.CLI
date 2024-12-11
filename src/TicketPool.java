public class TicketPool {

    private int ticketsAvailable;
    private final int maxTicketCapacity;
    private final Object condition = new Object();

    public TicketPool(int ticketsAvailable, int maxTicketCapacity) {
        this.ticketsAvailable = ticketsAvailable;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void addTickets(int amount) {
        synchronized (condition) {
            while (ticketsAvailable + amount > maxTicketCapacity) {
                try {
                    Logger.log("Ticket pool full. Vendor is waiting...");
                    Logger.log("");
                    condition.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            ticketsAvailable += amount;
            Logger.log("Added " + amount + " tickets. Total tickets: " + ticketsAvailable);
            Logger.log("");
            condition.notifyAll();
        }
    }

    public boolean purchaseTicket(int ticketsRequested) {
        synchronized (condition) {
            while (ticketsAvailable < ticketsRequested) {
                try {
                    Logger.log("Not enough tickets available. Customer is waiting...");
                    Logger.log("");
                    condition.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            ticketsAvailable -= ticketsRequested;
            Logger.log(ticketsRequested + " ticket(s) purchased. Tickets remaining: " + ticketsAvailable);
            Logger.log("");
            condition.notifyAll();
            return true;
        }
    }

}
