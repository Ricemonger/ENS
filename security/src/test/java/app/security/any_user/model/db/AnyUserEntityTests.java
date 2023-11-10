package app.security.any_user.model.db;

import app.security.any_users.model.db.AnyUserEntity;
import app.security.ens_users.model.db.EnsUserEntity;
import app.security.tg_users.model.db.TelegramUserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AnyUserEntityTests {

    private final static AnyUserEntity ENTITY = new AnyUserEntity(
            "1111",
            new EnsUserEntity("1111", "username", "password"),
            new TelegramUserEntity("1111", "9999"));

    private final static AnyUserEntity SAME_ENTITY = new AnyUserEntity(
            "1111",
            new EnsUserEntity("1111", "username", "password"),
            new TelegramUserEntity("1111", "9999"));

    private final static AnyUserEntity ALTERED_ACCOUNT_ID = new AnyUserEntity(
            "8888",
            new EnsUserEntity("1111", "username", "password"),
            new TelegramUserEntity("1111", "9999"));

    private final static AnyUserEntity ALTERED_ENS_USER_ENTITY = new AnyUserEntity(
            "1111",
            new EnsUserEntity("7777", "username", "password"),
            new TelegramUserEntity("1111", "9999"));

    private final static AnyUserEntity ALTERED_ANY_USER_ENTITY = new AnyUserEntity(
            "1111",
            new EnsUserEntity("1111", "username", "password"),
            new TelegramUserEntity("6666", "9999"));

    @Test
    public void equalsShouldBeTrueIfSame() {
        assertEquals(ENTITY, SAME_ENTITY);
    }

    @Test
    public void equalsShouldBeFalseIfNotSame() {
        assertNotEquals(ENTITY, ALTERED_ACCOUNT_ID);
        assertNotEquals(ENTITY, ALTERED_ENS_USER_ENTITY);
        assertNotEquals(ENTITY, ALTERED_ANY_USER_ENTITY);
    }
}
