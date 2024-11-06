package application;

import application.MainController;
import application.MainView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SellCommissionsAndBuyersPremiumTest {
    private static MainView view;
    private static MainController controller;

    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await();

        view = new MainView(FXCollections.observableArrayList());
        controller = new MainController(view);
    }

    @BeforeEach
    void setup() {
        // Reset inputs and labels
        Platform.runLater(() -> {
            view.getCommissionInput().clear();
            view.getPremiumInput().clear();
            view.getSellerCommissionLabel().setText("Seller's Commission: Not set");
            view.getBuyerPremiumLabel().setText("Buyer's Premium: Not set");
        });
    }

    @Test
    void testValidSellerCommission() {
        Platform.runLater(() -> {
            view.getCommissionInput().setText("10");
            view.getSetCommissionButton().fire();

            assertEquals("", view.getCommissionInput().getText(), "Commission input should be cleared.");
            assertEquals("Seller's Commission: 10.0%", view.getSellerCommissionLabel().getText(), "Seller's commission label should be updated.");
        });
    }

    @Test
    void testNonNumericSellerCommission() {
        Platform.runLater(() -> {
            view.getCommissionInput().setText("abc");
            view.getSetCommissionButton().fire();

            assertNotEquals("", view.getCommissionInput().getText(), "Commission input should not be cleared.");
            assertEquals("Seller's Commission: Not set", view.getSellerCommissionLabel().getText(), "Seller's commission label should not be updated.");
        });
    }

    @Test
    void testEmptySellerCommission() {
        Platform.runLater(() -> {
            view.getCommissionInput().setText("");
            view.getSetCommissionButton().fire();

            assertEquals("", view.getCommissionInput().getText(), "Commission input should not be cleared.");
            assertEquals("Seller's Commission: Not set", view.getSellerCommissionLabel().getText(), "Seller's commission label should not be updated.");
        });
    }

    @Test
    void testNegativeSellerCommission() {
        Platform.runLater(() -> {
            view.getCommissionInput().setText("-5");
            view.getSetCommissionButton().fire();

            // Check that the input was not cleared and that an error message was shown
            assertEquals("-5", view.getCommissionInput().getText(), "Commission input should not be cleared.");
            assertEquals("Seller's Commission: Not set", view.getSellerCommissionLabel().getText(), "Seller's commission label should not be updated.");
            
            // Add this line to print input content if the test fails
            System.out.println("Commission input text after attempt: " + view.getCommissionInput().getText());
        });
    }


    @Test
    void testDecimalSellerCommission() {
        Platform.runLater(() -> {
            view.getCommissionInput().setText("7.5");
            view.getSetCommissionButton().fire();

            assertEquals("", view.getCommissionInput().getText(), "Commission input should be cleared.");
            assertEquals("Seller's Commission: 7.5%", view.getSellerCommissionLabel().getText(), "Seller's commission label should be updated.");
        });
    }

    @Test
    void testValidBuyerPremium() {
        Platform.runLater(() -> {
            view.getPremiumInput().setText("15");
            view.getSetPremiumButton().fire();

            assertEquals("", view.getPremiumInput().getText(), "Premium input should be cleared.");
            assertEquals("Buyer's Premium: 15.0%", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should be updated.");
        });
    }

    @Test
    void testNonNumericBuyerPremium() {
        Platform.runLater(() -> {
            view.getPremiumInput().setText("xyz");
            view.getSetPremiumButton().fire();

            assertNotEquals("", view.getPremiumInput().getText(), "Premium input should not be cleared.");
            assertEquals("Buyer's Premium: Not set", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should not be updated.");
        });
    }

    @Test
    void testEmptyBuyerPremium() {
        Platform.runLater(() -> {
            view.getPremiumInput().setText("");
            view.getSetPremiumButton().fire();

            assertEquals("", view.getPremiumInput().getText(), "Premium input should not be cleared.");
            assertEquals("Buyer's Premium: Not set", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should not be updated.");
        });
    }

    @Test
    void testNegativeBuyerPremium() {
        Platform.runLater(() -> {
            view.getPremiumInput().setText("-10");
            view.getSetPremiumButton().fire();

            assertEquals("-10", view.getPremiumInput().getText(), "Premium input should not be cleared.");
            assertEquals("Buyer's Premium: Not set", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should not be updated.");
        });
    }

    @Test
    void testDecimalBuyerPremium() {
        Platform.runLater(() -> {
            view.getPremiumInput().setText("12.5");
            view.getSetPremiumButton().fire();

            assertEquals("", view.getPremiumInput().getText(), "Premium input should be cleared.");
            assertEquals("Buyer's Premium: 12.5%", view.getBuyerPremiumLabel().getText(), "Buyer's premium label should be updated.");
        });
    }
}
