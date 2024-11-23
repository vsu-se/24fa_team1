import application.Category;
import application.Item;
import application.SellerReportView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class SellerReportViewTest {

    private SellerReportView sellerReportView;
    private TableView<Item> reportTable;

    @BeforeAll
    public static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.startup(() -> {
            latch.countDown();
        });

        latch.await();
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);


        Platform.runLater(() -> {
            sellerReportView = new SellerReportView();
            reportTable = sellerReportView.getReportTable();
            latch.countDown();
        });

        latch.await();
    }

    @Test
    public void testLayoutInitialization() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            VBox layout = sellerReportView.getLayout();
            assertNotNull(layout, "Layout should be initialized");
            assertEquals(1, layout.getChildren().size(), "Layout should contain one child");
            assertTrue(layout.getChildren().get(0) instanceof TableView, "Child should be a TableView");
            latch.countDown();
        });

        latch.await();
    }

    @Test
    public void testTableColumnsInitialization() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            ObservableList<TableColumn<Item, ?>> columns = reportTable.getColumns();
            assertNotNull(columns, "Columns list should not be null.");
            assertEquals(5, columns.size(), "Table should have 5 columns");

            assertEquals("Title", columns.get(0).getText(), "First column should be 'Title'");
            assertEquals("Category", columns.get(1).getText(), "Second column should be 'Category'");
            assertEquals("Condition", columns.get(2).getText(), "Third column should be 'Condition'");
            assertEquals("End Date", columns.get(3).getText(), "Fourth column should be 'End Date'");
            assertEquals("Price", columns.get(4).getText(), "Fifth column should be 'Price'");
            latch.countDown();
        });

        latch.await();
    }

    @Test
    public void testTableDataBinding() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {

            Category category = new Category("Test Category");
            Item item = new Item(
                    "Test Title",
                    "1kg",
                    "Test Description",
                    category,
                    "New",
                    "Tag1",
                    "Tag2",
                    "Tag3",
                    LocalDateTime.now(),
                    LocalDateTime.of(2023, 12, 31, 23, 59),
                    100.0
            );


            ObservableList<Item> items = FXCollections.observableArrayList(item);
            reportTable.setItems(items);

            assertEquals(1, reportTable.getItems().size(), "Table should contain one item");
            assertEquals("Test Title", reportTable.getItems().get(0).getTitle(), "Item title should match");
            assertEquals("Test Category", reportTable.getItems().get(0).getCategory().getName(), "Item category should match");
            assertEquals("New", reportTable.getItems().get(0).getCondition(), "Item condition should match");
            assertEquals("2023-12-31T23:59", reportTable.getItems().get(0).getEndDate().toString(), "Item end date should match");
            assertEquals(100.0, reportTable.getItems().get(0).getBuyItNowPrice(), "Item price should match");

            latch.countDown();
        });

        latch.await();
    }
}
