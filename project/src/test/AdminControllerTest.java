package test;

import application.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        adminController = new AdminController();
    }

    @Test
    public void testGetSystemTime() {
        assertNotNull(adminController.getSystemTime(), "System time should not be null.");
    }

    @Test
    public void testSetSystemTime() {
        LocalDateTime newTime = LocalDateTime.of(2024, 12, 31, 23, 59);
        adminController.setSystemTime(newTime);
        assertEquals(newTime, adminController.getSystemTime(), "System time should match the updated time.");
    }
}

