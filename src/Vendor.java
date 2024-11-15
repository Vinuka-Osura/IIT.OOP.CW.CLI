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

    public void stop() {
        running = false;
    }
    @Override
    public void run() {
        while (running) {
            if (!ticketSystem.isPaused()) { // Only add tickets if not paused
                ticketPool.addTickets(releaseRate);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Vendor thread interrupted....");
                break;
            }
        }
        System.out.println("Vendor thread stopped.");
    }
}
