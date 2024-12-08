
package application;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class ItemController {
    private final ItemManager itemManager;
    private final ComboBox<Item> itemComboBox;
    private final TextField itemNameField;
    private final TextField itemPriceField;
    private final Button addItemButton;

    public ItemController(ItemManager itemManager, ComboBox<Item> itemComboBox, TextField itemNameField, TextField itemPriceField, Button addItemButton) {
        this.itemManager = itemManager;
        this.itemComboBox = itemComboBox;
        this.itemNameField = itemNameField;
        this.itemPriceField = itemPriceField;
        this.addItemButton = addItemButton;

        setupItemManagement();
    }

    private void setupItemManagement() {
        // Bind ComboBox to the item list
        itemComboBox.setItems(itemManager.getItems());

        // Add item event handler
        addItemButton.setOnAction(event -> {
            String itemName = itemNameField.getText();
            double itemPrice;

            try {
                itemPrice = Double.parseDouble(itemPriceField.getText());
            } catch (NumberFormatException e) {
                System.out.println("Invalid price input.");
                return;
            }

            Item newItem = new Item(itemName, itemPrice, 0, 0, 0);
            if (itemManager.addItem(newItem)) {
                System.out.println("Item added: " + itemName);
                itemNameField.clear();
                itemPriceField.clear();
            } else {
                System.out.println("Failed to add item: Validation error.");
            }
        });
    }

    public void updateItemView() {
        // Refresh item-related views
        System.out.println("Updating item views...");
    }
}
