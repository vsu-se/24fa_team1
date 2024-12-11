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

public class SystemTest8 {
    private AuctionSystemController controller;
    private AuctionSystemView view;
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
            view = new AuctionSystemView();
            controller = view.getController();
            auctions = controller.getAuctions();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceValidBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            String category = "Electronics";
            categories.add(category);
            //add mock items
            Auction auction1 = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, controller.getClock());
            Auction auction2 = new Auction(new Item("PC", "2 kg", "A powerful PC", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 1000.0, controller.getClock());
            Auction auction3 = new Auction(new Item("Phone", "2 kg", "A powerful Phone", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 300.0, controller.getClock());
            auctions.add(auction1);
            auctions.add(auction2);
            auctions.add(auction3);
            
            //Place bid on item1
            boolean bidPlacedItem1 = auction1.placeBid(600.0);
            assertTrue(bidPlacedItem1);
            assertEquals(600.0, auction1.getCurrentBid());
            
            //repeat for other items
            boolean bidPlacedItem2 = auction2.placeBid(10000.0);
            assertTrue(bidPlacedItem2);
            assertEquals(10000.0, auction2.getCurrentBid());
            
            boolean bidPlacedItem3 = auction3.placeBid(900.0);
            assertTrue(bidPlacedItem3);
            assertEquals(900.0, auction3.getCurrentBid());
            
            //Would love to not do this manually, yk? maybe needs a fix
            auction1.setHasBidder(true);
            auction2.setHasBidder(true);
            auction3.setHasBidder(true);
            
            controller.updateProfileItemsDisplay();
            
            int numMyBids = view.getNumMyBids();
            assertEquals(3, numMyBids);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    
    
    

    @Test
    public void testNoActiveBids() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            
            int numMyBids = view.getNumMyBids();
            assertEquals(0, numMyBids);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testInactiveBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            String category = "Electronics";
            categories.add(category);
            //add mock items
            Auction auction1 = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, controller.getClock());
            auctions.add(auction1);
            
            //Place bid on item1
            boolean bidPlacedItem1 = auction1.placeBid(600.0);
            assertTrue(bidPlacedItem1);
            assertEquals(600.0, auction1.getCurrentBid());
            
            //Would love to not do this manually, yk? maybe needs a fix, would love to just call the event handler for the button that's normally clicked
            auction1.setHasBidder(true);
            
            controller.updateProfileItemsDisplay();
            
            int numMyBids = view.getNumMyBids();
            assertEquals(1, numMyBids);
            
            //Skip time until after end-date
            auction1.setEndDate(LocalDateTime.now().minusDays(1));
            controller.updateItemsDisplay();
            controller.updateProfileItemsDisplay();
            
            numMyBids = view.getNumMyBids();
            assertEquals(0, numMyBids);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}