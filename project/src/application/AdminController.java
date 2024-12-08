
package application;

import java.time.LocalDateTime;

public class AdminController {
    private LocalDateTime systemTime;

    public AdminController() {
        this.systemTime = LocalDateTime.now();
    }

    // Manage system date/time
    public LocalDateTime getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(LocalDateTime newTime) {
        this.systemTime = newTime;
    }

    // Set seller's commission and buyer's premium
    public void setCategoryPremium(Category category, double sellerCommission, double buyerPremium) {
        category.setSellerCommission(sellerCommission);
        category.setBuyerPremium(buyerPremium);
    }
}
