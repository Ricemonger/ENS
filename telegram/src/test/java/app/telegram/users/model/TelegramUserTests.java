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

    private final static InputState IS = InputState.CONTACT_ID;

    private final static InputState AIS = InputState.NOTIFICATION_TEXT;

    private final static InputGroup IG = InputGroup.LINK;

    private final static InputGroup AIG = InputGroup.NOTIFICATION_ADD_ONE;

    private final static Boolean AC = true;

    private final static Boolean AAC = false;

    private final static String CP = "CUSTOM_PHRASE";

    private final static String ACP = "ALTERED_CUSTOM_PHRASE";

    private final static TelegramUser USER = new TelegramUser(ID, TT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUser SAME_USER = new TelegramUser(ID, TT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUser ALTERED_CHAT_ID = new TelegramUser(AID, TT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUser ALTERED_TELEGRAM_TOKEN = new TelegramUser(ID, ATT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUser ALTERED_TELEGRAM_TOKEN_DATE = new TelegramUser(ID, TT, ATD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUser ALTERED_SECURITY_TOKEN = new TelegramUser(ID, TT, TD, AST, SD, IS, IG, AC, CP);

    private final static TelegramUser ALTERED_SECURITY_TOKEN_DATE = new TelegramUser(ID, TT, TD, ST, ASD, IS, IG, AC, CP);

    private final static TelegramUser ALTERED_INPUT_STATE = new TelegramUser(ID, TT, TD, ST, SD, AIS, IG, AC, CP);

    private final static TelegramUser ALTERED_INPUT_GROUP = new TelegramUser(ID, TT, TD, ST, SD, IS, AIG, AC, CP);

    private final static TelegramUser ALTERED_ACTION_CONFIRMATION = new TelegramUser(ID, TT, TD, ST, SD, IS, AIG, AAC, CP);

    private final static TelegramUser ALTERED_CUSTOM_PHRASE = new TelegramUser(ID, TT, TD, ST, SD, IS, AIG, AC, ACP);

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
        assertNotEquals(USER, ALTERED_INPUT_STATE);
        assertNotEquals(USER, ALTERED_INPUT_GROUP);
        assertNotEquals(USER, ALTERED_ACTION_CONFIRMATION);
        assertNotEquals(USER, ALTERED_CUSTOM_PHRASE);
    }
}
