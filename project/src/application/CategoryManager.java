
package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryManager {
    private final ObservableList<Category> categories;

    public CategoryManager() {
        this.categories = FXCollections.observableArrayList();
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public boolean addCategory(Category category) {
        // Inline validation logic
        if (category == null || category.getName() == null || category.getName().isEmpty()) {
            return false; // Validation failed
        }
        return categories.add(category);
    }

    public boolean removeCategory(Category category) {
        return categories.remove(category);
    }
}
