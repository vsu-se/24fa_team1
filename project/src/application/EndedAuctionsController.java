package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class EndedAuctionsController {
    private List<Item> allItems;

    public EndedAuctionsController(List<Item> allItems) {
        this.allItems = allItems;
    }

    public ObservableList<Item> getEndedAuctions() {
        ObservableList<Item> endedAuctions = FXCollections.observableArrayList();
        for (Item item : allItems) {
            if (item.getEndDate().isBefore(LocalDateTime.now())) {
                endedAuctions.add(item);
            }
        }
        endedAuctions.sort(Comparator.comparing(Item::getEndDate).reversed());
        return endedAuctions;
    }
}