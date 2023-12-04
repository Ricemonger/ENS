package app.contact.model.db;

import app.utils.services.contact.Method;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ContactEntityTests {
    private final static ContactEntity CONTACT = new ContactEntity("9999", Method.SMS, "9999", "name");

    private final static ContactEntity SAME_CONTACT = new ContactEntity("9999", Method.SMS, "9999", "name");

    private final static ContactEntity ALTERED_ACCOUNT_ID = new ContactEntity("1111", Method.SMS, "9999", "name");

    private final static ContactEntity ALTERED_METHOD = new ContactEntity("9999", Method.TELEGRAM, "9999", "name");

    private final static ContactEntity ALTERED_CONTACT_ID = new ContactEntity("9999", Method.SMS, "1111", "name");

    private final static ContactEntity ALTERED_NOTIFICATION_NAME = new ContactEntity("9999", Method.SMS, "9999", "eman");

    @Test
    void equalsShouldBeTrueIfSame() {
        assertEquals(CONTACT, SAME_CONTACT);
    }

    @Test
    void equalsShouldBeFalseIfNotSame() {
        assertNotEquals(CONTACT, ALTERED_ACCOUNT_ID);
        assertNotEquals(CONTACT, ALTERED_METHOD);
        assertNotEquals(CONTACT, ALTERED_CONTACT_ID);
        assertNotEquals(CONTACT, ALTERED_NOTIFICATION_NAME);
    }

}
