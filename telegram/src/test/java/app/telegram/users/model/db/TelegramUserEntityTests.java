package app.telegram.users.model.db;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TelegramUserEntityTests {

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

    private final static TelegramUserEntity ENTITY = new TelegramUserEntity(ID, TT, TD, ST, SD);

    private final static TelegramUserEntity SAME_ENTITY = new TelegramUserEntity(ID, TT, TD, ST, SD);

    private final static TelegramUserEntity ALTERED_CHAT_ID = new TelegramUserEntity(AID, TT, TD, ST, SD);

    private final static TelegramUserEntity ALTERED_TELEGRAM_TOKEN = new TelegramUserEntity(ID, ATT, TD, ST, SD);

    private final static TelegramUserEntity ALTERED_TELEGRAM_TOKEN_DATE = new TelegramUserEntity(ID, TT, ATD, ST, SD);

    private final static TelegramUserEntity ALTERED_SECURITY_TOKEN = new TelegramUserEntity(ID, TT, TD, AST, SD);

    private final static TelegramUserEntity ALTERED_SECURITY_TOKEN_DATE = new TelegramUserEntity(ID, TT, TD, ST, ASD);

    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(ENTITY, SAME_ENTITY);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(ENTITY, ALTERED_CHAT_ID);
        assertNotEquals(ENTITY, ALTERED_TELEGRAM_TOKEN);
        assertNotEquals(ENTITY, ALTERED_TELEGRAM_TOKEN_DATE);
        assertNotEquals(ENTITY, ALTERED_SECURITY_TOKEN);
        assertNotEquals(ENTITY, ALTERED_SECURITY_TOKEN_DATE);
    }
}
