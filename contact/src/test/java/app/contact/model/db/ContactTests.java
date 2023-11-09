package app.contact.model.db;

import app.contact.model.Contact;
import app.contact.model.Method;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ContactTests {

    private final Contact CONTACT = new Contact("9999", Method.SMS, "9999", "name");

    private final Contact SAME_CONTACT = new Contact("9999", Method.SMS, "9999", "name");

    private final Contact ALTERED_ACCOUNT_ID = new Contact("1111", Method.SMS, "9999", "name");

    private final Contact ALTERED_METHOD = new Contact("9999", Method.TELEGRAM, "9999", "name");

    private final Contact ALTERED_CONTACT_ID = new Contact("9999", Method.SMS, "1111", "name");

    private final Contact ALTERED_NOTIFICATION_NAME = new Contact("9999", Method.SMS, "9999", "eman");

    @Test
    void equalsShouldBeTrueIfRight() {
        assertEquals(CONTACT, SAME_CONTACT);
    }

    @Test
    void equalsShouldBeFalseIfNot() {
        assertNotEquals(CONTACT, ALTERED_ACCOUNT_ID);
        assertNotEquals(CONTACT, ALTERED_METHOD);
        assertNotEquals(CONTACT, ALTERED_CONTACT_ID);
        assertNotEquals(CONTACT, ALTERED_NOTIFICATION_NAME);
    }
}
