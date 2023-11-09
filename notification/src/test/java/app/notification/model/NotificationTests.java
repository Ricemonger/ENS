package app.notification.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NotificationTests {

    private final static Notification NOTIFICATION = new Notification("1111", "name", "text");

    private final static Notification SAME_NOTIFICATION = new Notification("1111", "name", "text");

    private final static Notification ALTERED_NAME = new Notification("1111", "altered", "text");

    private final static Notification ALTERED_TEXT = new Notification("1111", "name", "altered");

    private final static Notification ALTERED_ACCOUNT_ID = new Notification("9999", "name", "text");

    @Test
    public void equalsShouldBeTrueIfRight() {
        assertEquals(NOTIFICATION, SAME_NOTIFICATION);
    }

    @Test
    public void equalsShouldBeFalseIfNotRight() {
        assertNotEquals(NOTIFICATION, ALTERED_NAME);
        assertNotEquals(NOTIFICATION, ALTERED_TEXT);
        assertNotEquals(NOTIFICATION, ALTERED_ACCOUNT_ID);
    }
}
