import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SystemClockTest {

    private SystemClock systemClock;

    @BeforeEach
    void setUp() {
        systemClock = new SystemClock();
    }

    @Test
    void testInitialTime() {
        assertNotNull(systemClock.getTime());
    }

    @Test
    void testSetTime() {
        LocalDateTime newTime = LocalDateTime.of(2023, 12, 25, 10, 0);
        systemClock.setTime(newTime);
        assertEquals(newTime, systemClock.getTime());
    }

    @Test
    void testTimeAdvancesWhenNotPaused() throws InterruptedException {
        LocalDateTime initialTime = systemClock.getTime();
        Thread.sleep(2000); // Wait for 2 seconds
        assertTrue(systemClock.getTime().isAfter(initialTime));
    }

    @Test
    void testTimeDoesNotAdvanceWhenPaused() throws InterruptedException {
        systemClock.setIsPaused(true);
        LocalDateTime initialTime = systemClock.getTime();
        Thread.sleep(2000); // Wait for 2 seconds
        assertEquals(initialTime, systemClock.getTime());
    }

    @Test
    void testPauseAndResume() throws InterruptedException {
        systemClock.setIsPaused(true);
        LocalDateTime pausedTime = systemClock.getTime();
        Thread.sleep(2000); // Wait for 2 seconds while paused
        assertEquals(pausedTime, systemClock.getTime());

        systemClock.setIsPaused(false);
        Thread.sleep(2000); // Wait for 2 seconds after resuming
        assertTrue(systemClock.getTime().isAfter(pausedTime));
    }

    @Test
    void testSetIsPaused() {
        systemClock.setIsPaused(true);
        assertTrue(systemClock.isPaused);

        systemClock.setIsPaused(false);
        assertFalse(systemClock.isPaused);
    }
}
