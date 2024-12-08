
package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Auction {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean concluded;
    private List<Item> items;

    public Auction(String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.concluded = false;
        this.items = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isOngoing() {
        return !concluded && endTime.isAfter(LocalDateTime.now());
    }

    public boolean isConcluded() { return concluded; }
    public void setConcluded(boolean concluded) { this.concluded = concluded; }

    public List<Item> getItems() { return items; }
    public void addItem(Item item) { items.add(item); }
}
