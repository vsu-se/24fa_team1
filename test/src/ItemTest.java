import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemTest {

    private Item item;

    @BeforeEach
    public void setUp() {
        item = new Item("Laptop", "2.5", "A high-end gaming laptop", "New");
    }

    @Test
    public void testConstructor() {
        assertNotNull(item);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Laptop", item.getTitle());
    }

    @Test
    public void testGetWeight() {
        assertEquals("2.5", item.getWeight());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A high-end gaming laptop", item.getDescription());
    }

    @Test
    public void testGetCondition() {
        assertEquals("New", item.getCondition());
    }
}