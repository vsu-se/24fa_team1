package application;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;

public class SystemClock {

	Timer timer;
	TimerTask task;
	LocalDateTime currentTime;
	boolean isPaused = false;
	public SystemClock() {
		timer = new Timer();
		currentTime = LocalDateTime.now();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(!isPaused) {
					currentTime = currentTime.plusSeconds(1);
				}
			}
		};
		timer.scheduleAtFixedRate(task,  0,  1000);
	}
	
	public LocalDateTime getTime() {
		return currentTime;
	}
	
	public void setTime(LocalDateTime now) {
		currentTime = now;
	}
	
	public void setIsPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
}