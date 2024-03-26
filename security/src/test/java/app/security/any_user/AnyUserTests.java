package app.security.any_user;

import app.security.any_users.AnyUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnyUserTests {

    private final static AnyUser USER = new AnyUser("9999");

    private final static AnyUser SAME_USER = new AnyUser("9999");

    private final static AnyUser ALTERED_ACCOUNT_ID = new AnyUser("1111");

    @Test
    public void equalsShouldBeTrueIfSame() {
        assertEquals(USER, SAME_USER);
    }

    @Test
    public void equalsShouldBeFalseIfNotSame() {
        assertEquals(USER, ALTERED_ACCOUNT_ID);
    }
}
