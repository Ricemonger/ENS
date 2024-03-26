package app.security.tg_users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TelegramUserTests {

    private final static TelegramUser USER = new TelegramUser("9999", "1111");

    private final static TelegramUser SAME_USER = new TelegramUser("9999", "1111");

    private final static TelegramUser ALTERED_ACCOUNT_ID = new TelegramUser("7777", "1111");

    private final static TelegramUser ALTERED_CHAT_ID = new TelegramUser("9999", "8888");

    @Test
    public void equalsShouldBeTrueIfSame() {
        assertEquals(USER, SAME_USER);
    }

    @Test
    public void equalsShouldBeFalseIfNotSame() {
        assertNotEquals(USER, ALTERED_ACCOUNT_ID);
        assertNotEquals(USER, ALTERED_CHAT_ID);
    }
}
