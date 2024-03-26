package app.telegram.users.model.db;

import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
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

    private final static InputState IS = InputState.CONTACT_ID;

    private final static InputState AIS = InputState.NOTIFICATION_TEXT;

    private final static InputGroup IG = InputGroup.LINK;

    private final static InputGroup AIG = InputGroup.NOTIFICATION_ADD_ONE;

    private final static Boolean AC = true;

    private final static Boolean AAC = false;

    private final static String CP = "CUSTOM_PHRASE";

    private final static String ACP = "ALTERED_CUSTOM_PHRASE";

    private final static TelegramUserEntity ENTITY = new TelegramUserEntity(ID, TT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUserEntity SAME_ENTITY = new TelegramUserEntity(ID, TT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUserEntity ALTERED_CHAT_ID = new TelegramUserEntity(AID, TT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUserEntity ALTERED_TELEGRAM_TOKEN = new TelegramUserEntity(ID, ATT, TD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUserEntity ALTERED_TELEGRAM_TOKEN_DATE = new TelegramUserEntity(ID, TT, ATD, ST, SD, IS, IG, AC, CP);

    private final static TelegramUserEntity ALTERED_SECURITY_TOKEN = new TelegramUserEntity(ID, TT, TD, AST, SD, IS, IG, AC, CP);

    private final static TelegramUserEntity ALTERED_SECURITY_TOKEN_DATE = new TelegramUserEntity(ID, TT, TD, ST, ASD, IS, IG, AC, CP);

    private final static TelegramUserEntity ALTERED_INPUT_STATE = new TelegramUserEntity(ID, TT, TD, ST, SD, AIS, IG, AC, CP);

    private final static TelegramUserEntity ALTERED_INPUT_GROUP = new TelegramUserEntity(ID, TT, TD, ST, SD, IS, AIG, AC, CP);

    private final static TelegramUserEntity ALTERED_ACTION_CONFIRMATION = new TelegramUserEntity(ID, TT, TD, ST, SD, IS, AIG, AAC, CP);

    private final static TelegramUserEntity ALTERED_CUSTOM_PHRASE = new TelegramUserEntity(ID, TT, TD, ST, SD, IS, AIG, AC, ACP);

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
        assertNotEquals(ENTITY, ALTERED_INPUT_STATE);
        assertNotEquals(ENTITY, ALTERED_INPUT_GROUP);
        assertNotEquals(ENTITY, ALTERED_ACTION_CONFIRMATION);
        assertNotEquals(ENTITY, ALTERED_CUSTOM_PHRASE);
    }
}
