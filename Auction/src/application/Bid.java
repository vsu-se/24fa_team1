package application;

import java.time.LocalDateTime;

public class Bid {
	private double amount;
	private LocalDateTime time;
	private boolean isBIN;
	
	public Bid(double amount, LocalDateTime time, boolean isBIN) {
		this.amount = amount;
		this.time = time;
		this.isBIN = isBIN;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	
	public boolean getIsBIN() {
		return isBIN;
	}
}
