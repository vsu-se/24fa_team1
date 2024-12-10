
package application;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private List<Item> items;

    public ItemManager() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getAllItems() {
        return items;
    }

    public Item findItemByTitle(String title) {
        return items.stream().filter(i -> i.getTitle().equals(title)).findFirst().orElse(null);
    }
}
