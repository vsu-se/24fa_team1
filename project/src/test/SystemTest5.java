package test;

import application.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SystemTest5 {
	private AddItemView view;
    private ObservableList<String> categories;
    private ObservableList<Auction> auctions;
    private Tab createItemTab;
    private AuctionSystemController controller;

    @BeforeEach
    public void setup() {
    	categories = FXCollections.observableArrayList("Electronics");
        auctions = FXCollections.observableArrayList();
        AuctionSystemView auctionSystemView = new AuctionSystemView();
        view = new AddItemView(auctionSystemView);
        controller = view.getController();

    }
    @BeforeAll
    static void initToolkit() throws Exception {
    	AuctionStatePersistence.canWrite = false;
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await();
    }

        @Test
        public void testSingleItem() throws Exception {
	        auctions.clear();
	        Item item = new Item("Single Item", "1 kg", "Description", "New");
	        auctions.add(new Auction(item, "Category", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 100.0, 0.00, controller.getClock()));
	        controller.updateItemsDisplay();
	        assertEquals(1, auctions.size());
	        assertEquals("Single Item", auctions.get(0).getItem().getTitle());
        }
    }