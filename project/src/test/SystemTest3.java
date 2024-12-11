package test;

import application.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SystemTest3 {
    private static AuctionSystemView view;
    private static AuctionSystemController controller;

    @BeforeAll
    static void initToolkit() throws Exception {
    	AuctionStatePersistence.canWrite = false;
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await();

        view = new AuctionSystemView();
        controller = new AuctionSystemController(view);
    }

    @BeforeEach
    void setup() throws Exception {
        // Reset inputs and labels
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCommissionInput().clear();
            view.getPremiumInput().clear();
            view.getBuyerPremiumLabel().setText("Buyer's Premium: Not set");
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testValidBuyerPremium() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getPremiumInput().setText("15");
            view.getSetPremiumButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("", view.getPremiumInput().getText(), "Premium input should be cleared.");
        assertEquals("Buyer's Premium: 15.00%", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should be updated.");
    }

    @Test
    void testNonNumericBuyerPremium() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getPremiumInput().setText("xyz");
            view.getSetPremiumButton().fire();
            latch.countDown();
        });
        latch.await();

        assertNotEquals("", view.getPremiumInput().getText(), "Premium input should not be cleared.");
        assertEquals("Buyer's Premium: Not set", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should not be updated.");
    }

    @Test
    void testEmptyBuyerPremium() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getPremiumInput().setText("");
            view.getSetPremiumButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("", view.getPremiumInput().getText(), "Premium input should not be cleared.");
        assertEquals("Buyer's Premium: Not set", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should not be updated.");
    }

    @Test
    void testNegativeBuyerPremium() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getPremiumInput().setText("-10");
            view.getSetPremiumButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("-10", view.getPremiumInput().getText(), "Premium input should not be cleared.");
        assertEquals("Buyer's Premium: Not set", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should not be updated.");
    }

    @Test
    void testDecimalBuyerPremium() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getPremiumInput().setText("12.5");
            view.getSetPremiumButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("", view.getPremiumInput().getText(), "Premium input should be cleared.");
        assertEquals("Buyer's Premium: 12.50%", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should be updated.");
    }
}