package test;

import application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ItemControllerTest {
    private ItemView view;
    private ItemController controller;
    private ObservableList<Category> categories;
    private ObservableList<Item> items; // Add this field
    private TabPane tabPane;
    private Tab createItemTab;
    private MainController mainController; // Add this field

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
        categories = FXCollections.observableArrayList(new Category("Electronics"));
        items = FXCollections.observableArrayList(); // Initialize this field
        view = new ItemView(categories);
        tabPane = new TabPane();
        createItemTab = new Tab();
        mainController = new MainController(new MainView(categories)); // Initialize this field

        controller = new ItemController(view, categories, tabPane, createItemTab, items, mainController); // Pass items and mainController to the constructor
    }

    @Test
    void testCreateItem() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField titleInput = view.getTitleInput();
            titleInput.setText("Laptop");

            TextField weightInput = view.getWeightInput();
            weightInput.setText("2.5");

            ComboBox<String> weightUnitComboBox = view.getWeightUnitComboBox();
            weightUnitComboBox.getItems().addAll("kg");
            weightUnitComboBox.setValue("kg");

            view.getDescriptionInput().setText("A high-end gaming laptop.");
            view.getCategoryComboBox().setValue(categories.get(0));
            view.getConditionComboBox().getItems().addAll("New", "Used");
            view.getConditionComboBox().setValue("New");

            view.getTag1Input().setText("Gaming");
            view.getTag2Input().setText("Laptop");
            view.getTag3Input().setText("Electronics");

            view.getEndDatePicker().setValue(LocalDate.now().plusDays(1));
            view.getEndTimeInput().setText("12:00");
            view.getBuyItNowPriceInput().setText("1500");

            Button createItemButton = view.getCreateItemButton();
            createItemButton.fire();

            // Verify that the tab is closed after item creation
            assertFalse(tabPane.getTabs().contains(createItemTab));
            latch.countDown();
        });
        latch.await();
    }
}
