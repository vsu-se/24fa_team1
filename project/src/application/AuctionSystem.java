import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuctionSystem {
	private ObservableList<String> categories;
	private ObservableList<String> concludedCategories;
	private ObservableList<Auction> auctions;
	private SystemClock clock = new SystemClock();
	private double buyersPremium;
	private double sellerCommission;
	
	public AuctionSystem() {
		buyersPremium = 0.00;
        sellerCommission = 0.00;
		categories = FXCollections.observableArrayList();
        concludedCategories = FXCollections.observableArrayList();
        auctions = FXCollections.observableArrayList();
	}
	
	public ObservableList<String> getCategories(){
		return categories;
	}

	public LocalDateTime getTime() {
		return clock.getTime();
	}

	public SystemClock getClock() {
		return clock;
	}
	
	public ObservableList<Auction> getAuctions(){
		return auctions;
	}
	
	public double getBuyersPremium() {
		return buyersPremium;
	}
	
	public void setBuyersPremium(double premium) {
		this.buyersPremium = premium;
	}
	
	public double getSellerCommission() {
		return sellerCommission;
	}
	
	public void setSellerCommission(double commission) {
		this.sellerCommission = commission;
	}

	public ObservableList<String> getConcludedCategories() {
		return concludedCategories;
	}
	
	public void addAuction(Auction a) {
		auctions.add(a);
	}
	
	
}
