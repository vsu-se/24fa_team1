package test;

import application.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class SystemTest9 {
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
    public void testEndOneAuctionWithOneAuctionActive() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            String category = "Electronics";
            categories.add(category);
            //add mock item
            Auction auction1 = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, controller.getClock());
            auctions.add(auction1);
            
            //Make sure item is active
            assertEquals(auctions.size(), 1);
            assertTrue(auction1.isActive());
            
            //Skip time until after end-date
            controller.getClock().setTime(LocalDateTime.now().plusDays(2));
            controller.getScheduler().scheduleNextUpdate();
            
            //Make sure item is inactive
            assertEquals(auctions.size(), 1);
            assertFalse(auction1.isActive());
            
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testEndOneAuctionWithMultipleAuctionsActive() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            String category = "Electronics";
            categories.add(category);
            //add mock items
            Auction auction1 = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, controller.getClock());
            Auction auction2 = new Auction(new Item("PC", "2 kg", "A powerful PC", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, 1000.0, controller.getClock());
            Auction auction3 = new Auction(new Item("Phone", "2 kg", "A powerful Phone", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, 300.0, controller.getClock());
            auctions.add(auction1);
            auctions.add(auction2);
            auctions.add(auction3);
            
            //Make sure item is active
            assertEquals(auctions.size(), 3);
            assertTrue(auction1.isActive());
            
            //Skip time until after end-date
            controller.getClock().setTime(LocalDateTime.now().plusDays(2));
            controller.getScheduler().scheduleNextUpdate();
            
            //Make sure item is inactive
            assertEquals(auctions.size(), 3);
            assertFalse(auction1.isActive());
            
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testEndTwoAuctionsWithMultipleAuctionsActive() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            String category = "Electronics";
            categories.add(category);
            //add mock items
            Auction auction1 = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, controller.getClock());
            Auction auction2 = new Auction(new Item("PC", "2 kg", "A powerful PC", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, 1000.0, controller.getClock());
            Auction auction3 = new Auction(new Item("Phone", "2 kg", "A powerful Phone", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, 300.0, controller.getClock());
            auctions.add(auction1);
            auctions.add(auction2);
            auctions.add(auction3);
            
            //Make sure items are active
            assertEquals(auctions.size(), 3);
            assertTrue(auction1.isActive() && auction2.isActive() && auction3.isActive());
            
            //Skip time until after end-date of first item
            controller.getClock().setTime(LocalDateTime.now().plusDays(2));
            controller.getScheduler().scheduleNextUpdate();
            
            //Make sure first item is inactive
            assertEquals(auctions.size(), 3);
            assertFalse(auction1.isActive());
            //Make sure the rest of the items are still active
            assertTrue(auction2.isActive() && auction3.isActive());
            
            //Skip time until after end-date of second item
            controller.getClock().setTime(LocalDateTime.now().plusDays(4));
            controller.getScheduler().scheduleNextUpdate();
            
            //Make sure second and first items are inactive
            assertEquals(auctions.size(), 3);
            assertFalse(auction1.isActive() && auction2.isActive());
            
            //Make sure third item is still active
            assertTrue(auction3.isActive());
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testEndTwoAuctionsSimultaneously() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            String category = "Electronics";
            categories.add(category);
            //add mock items
            Auction auction1 = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, controller.getClock());
            Auction auction2 = new Auction(new Item("PC", "2 kg", "A powerful PC", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 1000.0, controller.getClock());
            auctions.add(auction1);
            auctions.add(auction2);
            
            //Make sure both items are active
            assertEquals(auctions.size(), 2);
            assertTrue(auction1.isActive() && auction2.isActive());
            
            //Skip time until after end-date
            controller.getClock().setTime(LocalDateTime.now().plusDays(2));
            controller.getScheduler().scheduleNextUpdate();
            
            //Make sure both items are inactive
            assertEquals(auctions.size(), 2);
            assertFalse(auction1.isActive() && auction2.isActive());
            
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}