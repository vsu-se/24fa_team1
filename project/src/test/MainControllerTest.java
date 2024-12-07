package test;

import application.*;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {
    private MainView view;
    private MainController controller;
    private ObservableList<Category> categories;

    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            // No need to do anything here
            latch.countDown();
        });
        latch.await();
    }

    @BeforeEach
    void setUp() throws Exception {
        categories = FXCollections.observableArrayList();
        view = new MainView(categories);
        controller = new MainController(view,  new SystemClock());
    }

    @Test
    void testAddCategory() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField categoryInput = view.getCategoryInput();
            categoryInput.setText("Electronics");

            Button addButton = view.getAddButton();
            addButton.fire();

            assertEquals(1, controller.getCategories().size());
            assertEquals("Electronics", controller.getCategories().get(0).getName());
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testSetBuyerPremium() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField premiumInput = view.getPremiumInput();
            premiumInput.setText("10");

            Button setPremiumButton = view.getSetPremiumButton();
            setPremiumButton.fire();

            assertEquals(10.0, controller.getBuyerPremium());
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testSetSellerCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField commissionInput = view.getCommissionInput();
            commissionInput.setText("5");

            Button setCommissionButton = view.getSetCommissionButton();
            setCommissionButton.fire();

            assertEquals(5.0, controller.getSellerCommission());
            latch.countDown();
        });
        latch.await();
    }
}
