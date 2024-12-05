package application;

import java.time.LocalDateTime;

class Bid {
    private double amount;
    private LocalDateTime dateTime;
    private String username;

    public Bid(double amount, LocalDateTime dateTime, String username) {
        this.amount = amount;
        this.dateTime = dateTime;
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getUsername() {
        return username;
    }
}
