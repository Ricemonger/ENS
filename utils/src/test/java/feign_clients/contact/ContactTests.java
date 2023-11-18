package feign_clients.contact;

import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.Method;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ContactTests {

    private final static Contact CONTACT = new Contact(Method.SMS, "CONTACT_ID", "NOTIFICATION_NAME");

    private final static Contact SAME_CONTACT = new Contact(Method.SMS, "CONTACT_ID", "NOTIFICATION_NAME");

    private final static Contact ALTERED_METHOD = new Contact(Method.VIBER, "CONTACT_ID", "NOTIFICATION_NAME");

    private final static Contact ALTERED_CONTACT_ID = new Contact(Method.SMS, "DI_TCATNOC", "NOTIFICATION_NAME");

    private final static Contact ALTERED_NOTIFICATION_NAME = new Contact(Method.SMS, "CONTACT_ID", "EMAN_NOITACIFITON");

    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(CONTACT, SAME_CONTACT);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(CONTACT, ALTERED_METHOD);
        assertNotEquals(CONTACT, ALTERED_CONTACT_ID);
        assertNotEquals(CONTACT, ALTERED_NOTIFICATION_NAME);
    }
}
