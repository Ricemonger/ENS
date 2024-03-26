package app.telegram.users.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TelegramUserJwtUtilTests {
    private final static Long CHAT_ID = 9999L;

    private final static Long ANOTHER_CHAT_ID = 1111L;

    private final static String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5OTk5IiwiaWF0IjoxNzExMzcxMDk3LCJleHAiOjE3MTEzNzQ2OTd9.fxb5L27gFGtq5hLh57ASsgcNvwNrzvgExAENUdBlHGU";

    private final static String MALFORMED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.Xu4OxSsBTD4j3C0BNvfSr6SUEZKy6xtnq_dJoHuw2vc";

    private String JUST_GENERATED_TOKEN;

    private String JUST_GENERATED_TOKEN_CORRUPTED;

    @Autowired
    private TelegramUserJwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        JUST_GENERATED_TOKEN = jwtUtil.generateToken(CHAT_ID);
        JUST_GENERATED_TOKEN_CORRUPTED = JUST_GENERATED_TOKEN.substring(0, JUST_GENERATED_TOKEN.length() - 1);
    }


    @Test
    public void generatedTokenShouldHaveRightFormat() {
        //JWT TOKEN Regex
        assertTrue(JUST_GENERATED_TOKEN.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+(\\.[A-Za-z0-9-_.+/=]+)?$"));
    }

    @Test
    public void isTokenExpiredShouldReturnFalseForJustGeneratedToken() {
        assertFalse(jwtUtil.isTokenExpired(JUST_GENERATED_TOKEN));
    }

    @Test
    public void isTokenExpiredShouldReturnTrueForExpiredToken() {
        assertTrue(jwtUtil.isTokenExpired(EXPIRED_TOKEN));
    }

    @Test
    public void isTokenExpiredShouldThrowForInvalidTokens() {
        Executable executableCorrupted = () -> jwtUtil.isTokenExpired(JUST_GENERATED_TOKEN_CORRUPTED);
        assertThrows(Exception.class, executableCorrupted);

        Executable executableMalformed = () -> jwtUtil.isTokenExpired(MALFORMED_TOKEN);
        assertThrows(Exception.class, executableMalformed);

        Executable executableEmpty = () -> jwtUtil.isTokenExpired("");
        assertThrows(Exception.class, executableEmpty);

        Executable executableNull = () -> jwtUtil.isTokenExpired(null);
        assertThrows(Exception.class, executableNull);
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldReturnTrueForJustGeneratedToken() {
        assertTrue(jwtUtil.isTokenValidAndContainsChatId(JUST_GENERATED_TOKEN, CHAT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldHandleTokenWithBearer() {
        assertTrue(jwtUtil.isTokenValidAndContainsChatId("Bearer " + JUST_GENERATED_TOKEN, CHAT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldReturnFalseForExpiredToken() {
        assertFalse(jwtUtil.isTokenValidAndContainsChatId(EXPIRED_TOKEN, CHAT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldReturnFalseForInvalidTokens() {
        assertFalse(jwtUtil.isTokenValidAndContainsChatId(MALFORMED_TOKEN, CHAT_ID));
        assertFalse(jwtUtil.isTokenValidAndContainsChatId(JUST_GENERATED_TOKEN_CORRUPTED, CHAT_ID));
        assertFalse(jwtUtil.isTokenValidAndContainsChatId(null, CHAT_ID));
        assertFalse(jwtUtil.isTokenValidAndContainsChatId("", CHAT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldReturnFalseAnotherUserAccountId() {
        assertFalse(jwtUtil.isTokenValidAndContainsChatId(JUST_GENERATED_TOKEN, ANOTHER_CHAT_ID));
    }

    @Test
    public void extractExpirationShouldReturnRightTime() {
        long expectedExpirationTime = new Date().getTime() + TelegramUserJwtUtil.EXPIRATION_TIME;
        Instant expectedExpiration = new Date(expectedExpirationTime).toInstant();

        Instant trueExpiration = jwtUtil.extractExpiration(JUST_GENERATED_TOKEN).toInstant();

        Duration tolerance = Duration.ofSeconds(10);

        boolean datesAreEqual =
                Math.abs(Duration.between(expectedExpiration, trueExpiration).toMillis()) <= tolerance.toMillis();

        assertTrue(datesAreEqual);
    }

    @Test
    public void extractExpirationShouldThrowForInvalidOrExpiredTokens() {
        Executable executableExpired = () -> jwtUtil.extractExpiration(EXPIRED_TOKEN);
        assertThrows(Exception.class, executableExpired);

        Executable executableMalformed = () -> jwtUtil.extractExpiration(MALFORMED_TOKEN);
        assertThrows(Exception.class, executableMalformed);

        Executable executableCorrupted = () -> jwtUtil.extractExpiration(JUST_GENERATED_TOKEN_CORRUPTED);
        assertThrows(Exception.class, executableCorrupted);

        Executable executableEmpty = () -> jwtUtil.extractExpiration("");
        assertThrows(Exception.class, executableEmpty);

        Executable executableNull = () -> jwtUtil.extractExpiration(null);
        assertThrows(Exception.class, executableNull);
    }

    @Test
    public void extractAccountIdShouldReturnRightId() {
        assertEquals(CHAT_ID, Long.parseLong(jwtUtil.extractChatId(JUST_GENERATED_TOKEN)));
    }

    @Test
    public void extractAccountIdShouldThrowForInvalidOrExpiredTokens() {
        Executable executableExpired = () -> jwtUtil.extractChatId(EXPIRED_TOKEN);
        assertThrows(Exception.class, executableExpired);

        Executable executableMalformed = () -> jwtUtil.extractChatId(MALFORMED_TOKEN);
        assertThrows(Exception.class, executableMalformed);

        Executable executableCorrupted = () -> jwtUtil.extractChatId(JUST_GENERATED_TOKEN_CORRUPTED);
        assertThrows(Exception.class, executableCorrupted);

        Executable executableEmpty = () -> jwtUtil.extractChatId("");
        assertThrows(Exception.class, executableEmpty);

        Executable executableNull = () -> jwtUtil.extractChatId(null);
        assertThrows(Exception.class, executableNull);
    }
}
