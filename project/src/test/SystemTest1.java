package test;
import application.*;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class SystemTest1 {
    private static boolean isJavaFXInitialized = false;
    private MainView mainView;
    private MainController mainController;
    private ObservableList<Category> categories;

    @BeforeAll
    public static void initToolkit() throws Exception {
        if (!isJavaFXInitialized) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> {
                latch.countDown();
            });
            latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX initialization
            isJavaFXInitialized = true;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            categories = FXCollections.observableArrayList();
            mainView = new MainView(categories);
            mainController = new MainController(mainView,  new SystemClock());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX initialization
    }

    @Test
    public void testAddCategory() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String categoryName = "Electronics";
            mainView.getCategoryInput().setText(categoryName);
            mainView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(mainController); // Ensure mainController is not null
        assertEquals(1, mainController.getCategories().size());
        assertEquals("Electronics", mainController.getCategories().get(0).getName());
    }
    
    @Test
    public void testAddCategoryDuplicateName() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String categoryName = "Electronics";
            mainController.getCategories().add(new Category(categoryName));
            mainView.getCategoryInput().setText(categoryName);
            mainView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(mainController); // Ensure mainController is not null
        assertEquals(1, mainController.getCategories().size());
    }

    @Test
    public void testAddCategoryEmptyName() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            mainView.getCategoryInput().setText("");
            mainView.getAddButton().fire();
            assertEquals(0, mainController.getCategories().size());
            latch.countDown();
        });
    }

}
