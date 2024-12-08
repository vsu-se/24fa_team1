
package application;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AuctionController {
    private final ItemManager itemManager;
    private final ComboBox<Auction> auctionComboBox;
    private final ComboBox<Item> availableItemsComboBox;
    private final Button startAuctionButton;
    private final TextField auctionEndTimeField;

    public AuctionController(ItemManager itemManager, ComboBox<Auction> auctionComboBox, ComboBox<Item> availableItemsComboBox, Button startAuctionButton, TextField auctionEndTimeField) {
        this.itemManager = itemManager;
        this.auctionComboBox = auctionComboBox;
        this.availableItemsComboBox = availableItemsComboBox;
        this.startAuctionButton = startAuctionButton;
        this.auctionEndTimeField = auctionEndTimeField;

        setupAuctionManagement();
    }

    private void setupAuctionManagement() {
        // Populate ComboBox with items available for auction
        availableItemsComboBox.setItems(itemManager.getItems());

        // Handle auction start event
        startAuctionButton.setOnAction(event -> {
            Item selectedItem = availableItemsComboBox.getValue();
            String endTime = auctionEndTimeField.getText();

            if (selectedItem == null || endTime == null || endTime.isEmpty()) {
                System.out.println("Invalid auction details.");
                return;
            }

            Auction newAuction = new Auction(selectedItem.getName(), selectedItem.getPrice(), endTime);
            auctionComboBox.getItems().add(newAuction);
            System.out.println("Auction started for item: " + selectedItem.getName() + " ending at: " + endTime);
        });
    }

    public void updateAuctionView() {
        // Refresh auction-related views
        System.out.println("Updating auction views...");
    }
}
