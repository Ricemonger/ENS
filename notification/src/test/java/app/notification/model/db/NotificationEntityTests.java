package app.notification.model.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NotificationEntityTests {

    private final static NotificationEntity NOTIFICATION = new NotificationEntity("1111", "name", "text");

    private final static NotificationEntity SAME_NOTIFICATION = new NotificationEntity("1111", "name", "text");

    private final static NotificationEntity ALTERED_ACCOUNT_ID = new NotificationEntity("9999", "name", "text");

    private final static NotificationEntity ALTERED_NAME = new NotificationEntity("1111", "altered", "text");

    private final static NotificationEntity ALTERED_TEXT = new NotificationEntity("1111", "name", "altered");

    @Test
    public void equalsShouldBeTrueIfSame() {
        assertEquals(NOTIFICATION, SAME_NOTIFICATION);
    }

    @Test
    public void equalsShouldBeFalseIfNotSame() {
        assertNotEquals(NOTIFICATION, ALTERED_ACCOUNT_ID);
        assertNotEquals(NOTIFICATION, ALTERED_NAME);
        assertNotEquals(NOTIFICATION, ALTERED_TEXT);
    }
}
