
package application;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    private final MainView view;
    private final SystemClock systemClock;

    public MainController(MainView view, SystemClock systemClock) {
        this.view = view;
        this.systemClock = systemClock;

        setupTimeManagement();
    }

    private void setupTimeManagement() {
        Label currentTimeLabel = view.getCurrentTimeLabel();
        TextField customTimeField = view.getCustomTimeField();
        Button setTimeButton = view.getSetTimeButton();
        Button realTimeButton = view.getRealTimeButton();

        // Periodically update the current time label
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    LocalDateTime currentTime = systemClock.getCurrentTime();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    currentTimeLabel.setText("Current Time: " + currentTime.format(formatter));
                });
            }
        }, 0, 1000); // Update every second

        // Set custom time button handler
        setTimeButton.setOnAction(event -> {
            String customTime = customTimeField.getText();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime parsedTime = LocalDateTime.parse(customTime, formatter);
                systemClock.setCustomTime(parsedTime);
                System.out.println("Custom time set: " + parsedTime);
            } catch (Exception e) {
                System.out.println("Invalid time format. Please use 'yyyy-MM-dd HH:mm'.");
            }
        });

        // Resume real-time button handler
        realTimeButton.setOnAction(event -> {
            systemClock.setRealTime();
            System.out.println("System resumed to real-time.");
        });
    }
}
