//Auction should have an Item, a start time, an end date, bid history, etc.
package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Auction {
	private Item item;
    private String category;
    private String tag1;
    private String tag2;
    private String tag3;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double buyItNowPrice;
    private double currentBid;
    private boolean active;
    private boolean hasBidder;
    private SystemClock clock;
    
    public Auction(Item item, String category, String tag1, String tag2, String tag3, LocalDateTime startDate, LocalDateTime endDate, Double buyItNowPrice, double initialBid, SystemClock clock) {
    	this.item = item;
    	this.category = category;
    	this.tag1 = tag1;
    	this.tag2 = tag2;
    	this.tag3 = tag3;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.buyItNowPrice = buyItNowPrice;
    	this.currentBid = initialBid;
    	this.clock = clock;
    	active = true;
    }
    
    public String getCategory() {
        return category;
    }

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Double getBuyItNowPrice() {
        return buyItNowPrice;
    }

    public Double getCurrentBid() {
        return currentBid;
    }

    public boolean isActive() {
		return active && LocalDateTime.now().isBefore(endDate);
    }
    
    public void checkAndSetInactive() {
        if (clock.getTime().isAfter(endDate)) {
            active = false;
        }
    }
    
    public boolean placeBid(double bidAmount) {
        if (bidAmount > currentBid) {
            currentBid = bidAmount;
            return true;
        }
        return false;
    }
    
    public boolean hasBidder() {
    	return hasBidder;
    }
    
    public double getSellersCommission(double sellersCommissionPercent) {
    	return currentBid * (sellersCommissionPercent / 100);
    }
    
    public double getShippingCost() {
    	return 10.00; //FIXME WHAT IS THIS SUPPOSED TO BE? DOES USER SPECIFY??
    }
    
    public double getBuyersPremium(double buyersPremiumPercent) {
    	return currentBid * (buyersPremiumPercent / 100);
    }

	public Item getItem() {
		return item;
	}

	public void setHasBidder(boolean hasBidder) {
		this.hasBidder = hasBidder;
		
	}
}
