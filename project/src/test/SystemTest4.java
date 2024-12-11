package test;

import application.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
public class SystemTest4 {
    private AddItemView view;
    private ObservableList<String> categories;
    private ObservableList<Auction> items;
    private Tab createItemTab;
    private AuctionSystemController mainController;

    @BeforeAll
    static void initToolkit() throws Exception {
    	AuctionStatePersistence.canWrite = false;
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await();
    }

    @BeforeEach
    void setUp() throws Exception {
        categories = FXCollections.observableArrayList("Electronics");
        items = FXCollections.observableArrayList();
        AuctionSystemView auctionSystemView = new AuctionSystemView();
        view = new AddItemView(auctionSystemView);
        VBox layout = new VBox();
        createItemTab = new Tab();
        mainController = new AuctionSystemController(auctionSystemView);
    }

    @Test
    void testCreateItem() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	LocalDateTime endDate = LocalDateTime.now().plusDays(1);
            Auction auction = new Auction(new Item("Laptop", "2.5 kg", "A powerful laptop", "New"), categories.get(0), "tag1", "tag2", "tag3", LocalDateTime.now(), endDate, 1500.0, 500.0, mainController.getClock());
            mainController.addAuction(auction);
            
            Auction createdAuction = mainController.getAuctions().get(0);
            
            assertEquals("Laptop", createdAuction.getItem().getTitle());
            assertEquals("2.5 kg", createdAuction.getItem().getWeight());
            assertEquals("A powerful laptop", createdAuction.getItem().getDescription());
            assertEquals(categories.get(0), createdAuction.getCategory());
            assertEquals("New", createdAuction.getItem().getCondition());
            assertEquals("tag1", createdAuction.getTag1());
            assertEquals("tag2", createdAuction.getTag2());
            assertEquals("tag3", createdAuction.getTag3());
            assertEquals(endDate, createdAuction.getEndDate());
            assertEquals(1500.0, createdAuction.getBuyItNowPrice());
            latch.countDown();
        });
        latch.await();
    }





}