import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SystemTest2 {
    private static AuctionSystemView view;
    private static AuctionSystemController controller;

    @BeforeAll
    static void initToolkit() throws Exception {
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
            view.getSellerCommissionLabel().setText("Seller's Commission: Not set");
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testValidSellerCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCommissionInput().setText("10.00");
            view.getSetCommissionButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("", view.getCommissionInput().getText(), "Commission input should be cleared.");
        assertEquals("Seller's Commission: 10.00%", view.getSellerCommissionLabel().getText(), "Seller's commission label should be updated.");
    }

    @Test
    void testNonNumericSellerCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCommissionInput().setText("abc");
            view.getSetCommissionButton().fire();
            latch.countDown();
        });
        latch.await();

        assertNotEquals("", view.getCommissionInput().getText(), "Commission input should not be cleared.");
        assertEquals("Seller's Commission: Not set", view.getSellerCommissionLabel().getText(), "Seller's commission label should not be updated.");
    }

    @Test
    void testEmptySellerCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCommissionInput().setText("");
            view.getSetCommissionButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("", view.getCommissionInput().getText(), "Commission input should not be cleared.");
        assertEquals("Seller's Commission: Not set", view.getSellerCommissionLabel().getText(), "Seller's commission label should not be updated.");
    }

    @Test
    void testNegativeSellerCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCommissionInput().setText("-5");
            view.getSetCommissionButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("-5", view.getCommissionInput().getText(), "Commission input should not be cleared.");
        assertEquals("Seller's Commission: Not set", view.getSellerCommissionLabel().getText(), "Seller's commission label should not be updated.");
    }

    @Test
    void testDecimalSellerCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCommissionInput().setText("7.50");
            view.getSetCommissionButton().fire();
            latch.countDown();
        });
        latch.await();

        assertEquals("", view.getCommissionInput().getText(), "Commission input should be cleared.");
        assertEquals("Seller's Commission: 7.50%", view.getSellerCommissionLabel().getText(), "Seller's commission label should be updated.");
    }
}