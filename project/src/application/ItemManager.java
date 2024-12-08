
package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemManager {
    private final ObservableList<Item> items;

    public ItemManager() {
        this.items = FXCollections.observableArrayList();
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public boolean addItem(Item item) {
        // Inline validation logic
        if (item == null || item.getName() == null || item.getName().isEmpty() ||
            item.getPrice() <= 0 || item.getShippingCost() < 0) {
            return false; // Validation failed
        }
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }
}
