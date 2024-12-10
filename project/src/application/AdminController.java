
package application;

import java.time.LocalDateTime;

public class AdminController {
    private LocalDateTime systemTime;

    public AdminController() {
        this.systemTime = LocalDateTime.now();
    }

    public LocalDateTime getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(LocalDateTime newTime) {
        this.systemTime = newTime;
    }
}
