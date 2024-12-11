package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.Auction;
import application.AuctionStatePersistence;
import application.AuctionSystemController;
import application.AuctionSystemView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class SystemTest15 {
	private ObservableList<Auction> auctions;
    private AuctionSystemController controller;
    private AuctionSystemView view;
    private ObservableList<String> categories;

    @BeforeAll
    public static void initToolkit() throws Exception {
    	AuctionStatePersistence.canWrite = false;
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await(5, TimeUnit.SECONDS);
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            categories = FXCollections.observableArrayList();
            view = new AuctionSystemView();
            controller = view.getController();
            auctions = controller.getAuctions();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
	@Test
	void testResumeTime() {
		//Mock Values
		LocalDate date = LocalDate.of(2025, 10, 11); //Oct. 11, 2025
		
		LocalTime time = LocalTime.of(10, 0, 0); //10:00 AM
		
		//Set Time
		controller.changeTime( date, time);
		
		//Verify
		assertEquals(LocalDateTime.of(date, time), controller.getClock().getTime());
		
		//Resume Time
		controller.resumeTime();
		
		//Verify
		assertEquals(LocalDateTime.now(), controller.getClock().getTime());
	}

}
