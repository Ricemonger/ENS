package app.security.ens_users.model.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EnsUserEntityTests {

    private final static EnsUserEntity ENTITY = new EnsUserEntity("1111", "username", "password");

    private final static EnsUserEntity SAME_ENTITY = new EnsUserEntity("1111", "username", "password");

    private final static EnsUserEntity ALTERED_ACCOUNT_ID = new EnsUserEntity("9999", "username", "password");

    private final static EnsUserEntity ALTERED_USERNAME = new EnsUserEntity("1111", "emanresu", "password");

    private final static EnsUserEntity ALTERED_PASSWORD = new EnsUserEntity("1111", "username", "drowssap");

    @Test
    public void equalsShouldBeTrueIfSame() {
        assertEquals(ENTITY, SAME_ENTITY);
    }

    @Test
    public void equalsShouldBeFalseIfNotSame() {
        assertNotEquals(ENTITY, ALTERED_ACCOUNT_ID);
        assertNotEquals(ENTITY, ALTERED_USERNAME);
        assertNotEquals(ENTITY, ALTERED_PASSWORD);
    }
}
