import java.util.concurrent.atomic.AtomicBoolean;

public class Customer implements Runnable, Comparable<Customer> {

    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final int priority;
    private final TicketSystem ticketSystem;
    //private volatile boolean running = true;
    private final AtomicBoolean active = new AtomicBoolean(true);

    public Customer(TicketPool ticketPool, int retrievalRate, int priority, TicketSystem ticketSystem) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.priority = priority;
        this.ticketSystem = ticketSystem;
    }

    public void stop() {
        active.set(false);
    }

    @Override
    public int compareTo(Customer other) {
        return Integer.compare(other.priority, this.priority); // Higher priority customers first
    }

    @Override
    public void run() {
        while (active.get() && !Thread.currentThread().isInterrupted()) {
            if (ticketSystem.isPaused()) {  // Check if the system is paused
                try {
                    Thread.sleep(100);  // Pause customer actions temporarily
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            if (!active.get()) break;

            // Simulate ticket purchase action
            if (!ticketPool.purchaseTicket()) {
                System.out.println("No tickets available for purchase.");
            }

            try {
                Thread.sleep(1000 / retrievalRate);  // Control retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Customer thread stopped.");  // Log stop for confirmation
    }
}
