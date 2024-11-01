package project.src.application;

public class Auction {
    private String itemName;
    private double startingBid;
    private double currentBid;

    public Auction(String itemName, double startingBid) {
        this.itemName = itemName;
        this.startingBid = startingBid;
        this.currentBid = startingBid;
    }

    public String getItemName() {
        return itemName;
    }

    public double getStartingBid() {
        return startingBid;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    @Override
    public String toString() {
        return itemName + " (Current Bid: $" + currentBid + ")";
    }
}