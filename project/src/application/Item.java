package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Item {
    private String title;
    private String weight;
    private String description;
    private Category category;
    private String condition;
    private String tag1;
    private String tag2;
    private String tag3;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double buyItNowPrice;
    private Double currentBid;
    private boolean active;
    private boolean hasBidder;
    private SystemClock clock;
    private List<Bid> bidHistory;

    public Item(String title, String weight, String description, Category category, String condition, String tag1, String tag2, String tag3, LocalDateTime startDate, LocalDateTime endDate, Double buyItNowPrice, Double initialBid, SystemClock clock  ) {
        this.title = title;
        this.weight = weight;
        this.description = description;
        this.category = category;
        this.condition = condition;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.startDate = startDate;
        this.endDate = endDate;
        this.buyItNowPrice = buyItNowPrice;
        this.currentBid = 0.0;
        this.active = true;
        this.currentBid = initialBid;
        this.clock= clock;
        this.bidHistory = new ArrayList<>();
    }

    // Getters and setters for all fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Double getBuyItNowPrice() {
        return buyItNowPrice;
    }

    public void setBuyItNowPrice(Double buyItNowPrice) {
        this.buyItNowPrice = buyItNowPrice;
    }

    public Double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Double currentBid) {
        this.currentBid = currentBid;
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
        public List<Bid> getBidHistory() {
            return bidHistory;
        }
        public String generateBidHistoryReport() {
            StringBuilder report = new StringBuilder();
            report.append("Bid History Report:\n");
            report.append("Bid Amount\tDate/Time\t\tUser Name\n");

            bidHistory.stream()
                    .sorted(Comparator.comparingDouble(Bid::getAmount).reversed())
                    .forEach(bid -> report.append(String.format("$%.2f\t\t%s\t%s\n", bid.getAmount(), bid.getDateTime(), bid.getUsername())));

            return report.toString();
        }


    public double calculateShippingCost() {
        double weightValue;
        String weightUnit;
        try {
            // Split the weight string into value and unit
            String[] parts = weight.split(" ");
            weightValue = Double.parseDouble(parts[0]);
            weightUnit = parts[1];
        } catch (Exception e) {
            // Handle the case where the weight string is not in the expected format
            return 0.0;
        }
        double costPerKg = 5.0; // Example cost per kg
        double costPerLb = 2.5; // Example cost per lb

        if (weightUnit.equals("kg")) {
            return weightValue * costPerKg;
        } else if (weightUnit.equals("lb")) {
            return weightValue * costPerLb;
        } else {
            return 0.0;
        }
    }


    public boolean hasBidder() {
        return hasBidder;
    }

    public void setHasBidder(boolean hasBidder) {
        this.hasBidder = hasBidder;
    }
}