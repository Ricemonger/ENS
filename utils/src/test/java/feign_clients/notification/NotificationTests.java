package feign_clients.notification;

import app.utils.feign_clients.notification.Notification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NotificationTests {

    private final static Notification NOTIFICATION = new Notification("NAME", "TEXT");

    private final static Notification SAME_NOTIFICATION = new Notification("NAME", "TEXT");

    private final static Notification ALTERED_NAME = new Notification("EMAN", "TEXT");

    private final static Notification ALTERED_TEXT = new Notification("NAME", "TXET");

    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(NOTIFICATION, SAME_NOTIFICATION);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(NOTIFICATION, ALTERED_NAME);
        assertNotEquals(NOTIFICATION, ALTERED_TEXT);
    }
}