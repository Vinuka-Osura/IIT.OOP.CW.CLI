import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

    private int ticketsAvailable;
    private final int maxTicketCapacity;
    private final Lock lock = new ReentrantLock();

    public TicketPool(int ticketsAvailable, int maxTicketCapacity) {
        this.ticketsAvailable = ticketsAvailable;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    //method for add tickets
    public void addTickets(int amount) {
        lock.lock();
        try {
            if (ticketsAvailable + amount <= maxTicketCapacity) {
                ticketsAvailable += amount;
                System.out.println("Added " + amount + " tickets. Total tickets: " + ticketsAvailable);
            } else {
                System.out.println("Ticket pool full. Unable to add more tickets.");
            }
        } finally {
            lock.unlock();
        }
    }

    //method for buy tickets
    public boolean purchaseTicket() {
        lock.lock();
        try {
            if (ticketsAvailable > 0) {
                ticketsAvailable--;
                System.out.println("Ticket purchased. Tickets remaining: " + ticketsAvailable);
                return true;
            } else {
                System.out.println("No tickets available for purchase.");
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    // Method to get the ticket count
    public int getTicketsAvailable() {
        lock.lock();
        try {
            return ticketsAvailable;
        } finally {
            lock.unlock();
        }
    }


}
