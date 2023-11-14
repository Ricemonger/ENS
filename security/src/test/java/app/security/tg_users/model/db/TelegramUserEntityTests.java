package app.security.tg_users.model.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TelegramUserEntityTests {

    private final static TelegramUserEntity ENTITY = new TelegramUserEntity("1111", "9999");

    private final static TelegramUserEntity SAME_ENTITY = new TelegramUserEntity("1111", "9999");

    private final static TelegramUserEntity ALTERED_ACCOUNT_ID = new TelegramUserEntity("8888", "9999");

    private final static TelegramUserEntity ALTERED_CHAT_ID = new TelegramUserEntity("1111", "7777");

    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(ENTITY, SAME_ENTITY);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(ENTITY, ALTERED_ACCOUNT_ID);
        assertNotEquals(ENTITY, ALTERED_CHAT_ID);
    }
}
