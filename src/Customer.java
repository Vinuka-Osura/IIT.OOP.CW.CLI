import java.util.concurrent.atomic.AtomicBoolean;

public class Customer implements Runnable {

    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final TicketSystem ticketSystem;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public Customer(TicketPool ticketPool, int retrievalRate, TicketSystem ticketSystem) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.ticketSystem = ticketSystem;
    }


    @Override
    public void run() {
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            if (ticketSystem.isPaused()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            if (!ticketPool.purchaseTicket(retrievalRate)) {
                Logger.log("Customer couldn't purchase " + retrievalRate + " ticket(s). Waiting for availability...");
            }

            try {
                Thread.sleep(3000); // Simulate delay between purchases
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        Logger.log("Customer thread stopped.");
    }
}
