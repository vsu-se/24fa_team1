package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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

class SystemTest13 {
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
	void testShowBidHistory() {
		String category = "Books";
        LocalDateTime startDate = controller.getClock().getTime();
        LocalDateTime endDate = controller.getClock().getTime().plusDays(7);
		//Mock Item
		Auction auction = new Auction(new Item("Book", "0.5kg", "A rare book", "New"), category, "books", "rare", "collectible", startDate, endDate, 100.0,  0.0, controller.getClock());
		auctions.add(auction);
		
		assertEquals(0, auction.getBidHistory().size());
		
		//Place Bid
		auction.placeBid(1000);
		//Place Bid
		auction.placeBid(2000);
		
		//Verify that both bids have been tracked
		assertEquals(2, auction.getBidHistory().size());
		assertEquals(1000, auction.getBidHistory().get(0).getAmount());
		assertEquals(2000, auction.getBidHistory().get(1).getAmount());
		
		
	}

}
