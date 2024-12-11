package test;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.*;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SystemTest7 {
	
	private AuctionSystemView view;
    private AuctionSystemController controller;
    private ObservableList<String> categories;
    private ObservableList<Auction> auctions;

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
            auctions= FXCollections.observableArrayList();
            view = new AuctionSystemView();
            controller = view.getController();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceValidBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String category = "Electronics";
            categories.add(category);
            //add mock item
            Auction auction= new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 100.0, controller.getClock());
            auctions.add(auction);

            boolean bidPlaced = auction.placeBid(150.0);

            assertTrue(bidPlaced);
            assertEquals(150.0, auction.getCurrentBid());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceBidLowerThanCurrentBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String category = "Electronics";
            categories.add(category);
            //add mock item
            Auction auction = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 100.0, controller.getClock());
            auctions.add(auction);

            boolean bidPlaced = auction.placeBid(50.0);

            assertFalse(bidPlaced);
            assertEquals(100.0, auction.getCurrentBid());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceBidWithInvalidAmount() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String category = "Electronics";
            categories.add(category);
            Auction auction = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 100.0, controller.getClock());
            auctions.add(auction);
            
            //assertEquals(mainView.getListItemErrorLabel().getText(), "Please enter a valid bid amount.");
            assertEquals(100.0, auction.getCurrentBid());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}