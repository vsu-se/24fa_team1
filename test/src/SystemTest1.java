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
    private AuctionSystemView auctionSystemView;
    private AuctionSystemController auctionSystemController;
    private ObservableList<String> categories;

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
            auctionSystemView = new AuctionSystemView();
            auctionSystemController = new AuctionSystemController(auctionSystemView);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX initialization
    }

    @Test
    public void testAddCategory() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String categoryName = "Electronics";
            auctionSystemView.getCategoryInput().setText(categoryName);
            auctionSystemView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(auctionSystemController); // Ensure AuctionSystemController is not null
        assertEquals(1, auctionSystemController.getCategories().size());
        assertEquals("Electronics", auctionSystemController.getCategories().get(0));
    }

    @Test
    public void testAddCategoryDuplicateName() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String categoryName = "Electronics";
            auctionSystemController.getCategories().add(categoryName);
            auctionSystemView.getCategoryInput().setText(categoryName);
            auctionSystemView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(auctionSystemController); // Ensure AuctionSystemController is not null
        assertEquals(1, auctionSystemController.getCategories().size());
    }

    @Test
    public void testAddCategoryEmptyName() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            auctionSystemView.getCategoryInput().setText("");
            auctionSystemView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(auctionSystemController); // Ensure AuctionSystemController is not null
        assertEquals(0, auctionSystemController.getCategories().size());
    }
}