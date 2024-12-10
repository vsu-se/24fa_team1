
package application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AuctionController {
    private List<Auction> auctions;

    public AuctionController() {
        this.auctions = new ArrayList<>();
    }

    // Add a new auction
    public void addAuction(Auction auction) {
        auctions.add(auction);
    }

    // Get all auctions
    public List<Auction> getAllAuctions() {
        return auctions;
    }

    // Remove an auction
    public void removeAuction(Auction auction) {
        auctions.remove(auction);
    }

    // Find an auction by name
    public Auction findAuctionByName(String name) {
        return auctions.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Sort auctions by their end time
    public List<Auction> getSortedAuctions() {
        auctions.sort(Comparator.comparing(Auction::getEndTime));
        return auctions;
    }

    // Add an item to an auction
    public boolean addItemToAuction(String auctionName, Item item) {
        Auction auction = findAuctionByName(auctionName);
        if (auction != null) {
            auction.addItem(item);
            return true;
        }
        return false;
    }
}
