
package test;

import application.*; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemControllerTest {
    private ItemManager itemManager;
    private CategoryManager categoryManager;

    @BeforeEach
    public void setUp() {
        itemManager = new ItemManager();
        categoryManager = new CategoryManager();
    }

    @Test
    public void testAddItem() {
        Item item = new Item("Laptop", "2kg", "High-end laptop", new Category("Electronics"),
                "New", "Tag1", "Tag2", "Tag3", null, null, 1200.0, 0.0, true, false);
        itemManager.addItem(item);
        List<Item> items = itemManager.getAllItems();
        assertTrue(items.contains(item), "Item should be added to the manager.");
    }

    @Test
    public void testFindItemByTitle() {
        Item item = new Item("Phone", "500g", "Smartphone", new Category("Electronics"),
                "New", "Tag1", "Tag2", "Tag3", null, null, 800.0, 0.0, true, false);
        itemManager.addItem(item);
        Item foundItem = itemManager.findItemByTitle("Phone");
        assertNotNull(foundItem, "Should find the item by title.");
        assertEquals("Phone", foundItem.getTitle(), "Title should match.");
    }

    @Test
    public void testAddCategory() {
        Category category = new Category("Books");
        categoryManager.addCategory(category);
        List<Category> categories = categoryManager.getAllCategories();
        assertTrue(categories.contains(category), "Category should be added to the manager.");
    }

    @Test
    public void testFindCategoryByName() {
        Category category = new Category("Furniture");
        categoryManager.addCategory(category);
        Category foundCategory = categoryManager.findCategoryByName("Furniture");
        assertNotNull(foundCategory, "Should find the category by name.");
        assertEquals("Furniture", foundCategory.getName(), "Name should match.");
    }
}
