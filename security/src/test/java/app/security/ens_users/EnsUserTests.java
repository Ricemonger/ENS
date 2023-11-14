package app.security.ens_users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EnsUserTests {

    private final static EnsUser USER = new EnsUser("1111", "username", "password");

    private final static EnsUser SAME_USER = new EnsUser("1111", "username", "password");

    private final static EnsUser ALTERED_ACCOUNT_ID = new EnsUser("9999", "username", "password");

    private final static EnsUser ALTERED_USERNAME = new EnsUser("1111", "emanresu", "password");

    private final static EnsUser ALTERED_PASSWORD = new EnsUser("1111", "username", "drowssap");


    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(USER, SAME_USER);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(USER, ALTERED_ACCOUNT_ID);
        assertNotEquals(USER, ALTERED_USERNAME);
        assertNotEquals(USER, ALTERED_PASSWORD);
    }
}
