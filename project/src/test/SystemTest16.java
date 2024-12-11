package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.Auction;
import application.AuctionStatePersistence;
import application.AuctionSystemController;
import application.AuctionSystemView;
import application.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class SystemTest16 {
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
	void testSaveState() throws Exception {
		AuctionStatePersistence.canWrite = true;
		double buyersPremium = 10;
		double sellerCommission = 20;
		
		//add mock category
		String category = "Electronics";
        categories.add(category);
        //add mock item
        Auction auction1 = new Auction(new Item("Laptop", "2 kg", "A powerful laptop", "New"), category, "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, controller.getClock());
        auctions.add(auction1);
        
        //add mock bids
        auction1.placeBid(1000);
        auction1.placeBid(2000);
		
		//Save State
        AuctionStatePersistence.saveState(categories, auctions, buyersPremium, sellerCommission, "testFile.txt");
        
        
        BufferedReader br = new BufferedReader(new FileReader("testFile.txt"));
		
        br.readLine();
        assertTrue(br.readLine().contains(category));
        br.readLine();
        assertTrue(br.readLine().contains(auction1.getItem().getTitle()));
        assertTrue(br.readLine().contains("1000"));
        assertTrue(br.readLine().contains("2000"));
        br.readLine();
        br.readLine();
        assertTrue(br.readLine().contains("10"));
        br.readLine();
        assertTrue(br.readLine().contains("20"));
	}

}
