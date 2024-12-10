package application;

import java.time.ZoneOffset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UpdateScheduler {
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> scheduledFuture;
	private AuctionSystemController controller;
	public UpdateScheduler(AuctionSystemController controller){
		scheduler = Executors.newScheduledThreadPool(1);
		this.controller = controller;
	}
	
	public void scheduleNextUpdate() {
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(false);
        }

        Auction nextExpiringAuction = controller.getNextExpiringAuction();
        if (nextExpiringAuction != null) {
            long delay = nextExpiringAuction.getEndDate().toEpochSecond(ZoneOffset.UTC) - controller.getClock().getTime().toEpochSecond(ZoneOffset.UTC);
            if (delay > 0) {
                scheduledFuture = scheduler.schedule(controller::checkAndUpdateAuctions, delay, TimeUnit.SECONDS);
            } else {
                controller.checkAndUpdateAuctions();
            }
        }
    }
	
	public void shutdownScheduler() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
