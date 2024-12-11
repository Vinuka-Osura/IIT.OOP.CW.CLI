public class Vendor implements Runnable {

    private final TicketPool ticketPool;
    private final int releaseRate;
    private final TicketSystem ticketSystem;
    private volatile boolean running = true;

    public Vendor(TicketPool ticketPool, int releaseRate, TicketSystem ticketSystem ) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.ticketSystem = ticketSystem;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            if (!ticketSystem.isPaused()) {
                ticketPool.addTickets(releaseRate);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        Logger.log("Vendor thread stopped.");
    }
}
