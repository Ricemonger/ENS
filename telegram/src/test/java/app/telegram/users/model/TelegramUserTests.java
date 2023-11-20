package app.telegram.users.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TelegramUserTests {

    private final static String ID = "CHAT_ID";

    private final static String AID = "ALTERED_CHAT_ID";

    private final static String TT = "TELEGRAM_TOKEN";

    private final static String ATT = "ALTERED_TELEGRAM_TOKEN";

    private final static Date TD = new Date(1_000);

    private final static Date ATD = new Date(1_100);

    private final static String ST = "SECURITY_TOKEN";

    private final static String AST = "ALTERED_SECURITY_TOKEN";

    private final static Date SD = new Date(2_000);

    private final static Date ASD = new Date(2_200);

    private final static TelegramUser USER = new TelegramUser(ID, TT, TD, ST, SD);

    private final static TelegramUser SAME_USER = new TelegramUser(ID, TT, TD, ST, SD);

    private final static TelegramUser ALTERED_CHAT_ID = new TelegramUser(AID, TT, TD, ST, SD);

    private final static TelegramUser ALTERED_TELEGRAM_TOKEN = new TelegramUser(ID, ATT, TD, ST, SD);

    private final static TelegramUser ALTERED_TELEGRAM_TOKEN_DATE = new TelegramUser(ID, TT, ATD, ST, SD);

    private final static TelegramUser ALTERED_SECURITY_TOKEN = new TelegramUser(ID, TT, TD, AST, SD);

    private final static TelegramUser ALTERED_SECURITY_TOKEN_DATE = new TelegramUser(ID, TT, TD, ST, ASD);

    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(USER, SAME_USER);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(USER, ALTERED_CHAT_ID);
        assertNotEquals(USER, ALTERED_TELEGRAM_TOKEN);
        assertNotEquals(USER, ALTERED_TELEGRAM_TOKEN_DATE);
        assertNotEquals(USER, ALTERED_SECURITY_TOKEN);
        assertNotEquals(USER, ALTERED_SECURITY_TOKEN_DATE);
    }
}
