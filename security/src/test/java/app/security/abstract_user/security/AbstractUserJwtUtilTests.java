package app.security.abstract_user.security;

import app.security.abstract_users.security.AbstractUserJwtUtil;
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
public class AbstractUserJwtUtilTests {

    private final static String ACCOUNT_ID = "9999";

    private final static String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5OTk5IiwiaWF0IjoxNjk5NjE0MDkwLCJleHAiOjE2OTk2MTQwOTB9.lXcNtsjLSU5gTJUcgsYuHmSexEEndk-EsnhnInhHlwI";

    private final static String MALFORMED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5OTk5IiwiaWF0IjoxNjk5NjE0MDkwLCJleHAiOjE2OTk2MTQwOTB9";

    private String JUST_GENERATED_TOKEN;

    private String JUST_GENERATED_TOKEN_CORRUPTED;

    private final static String ANOTHER_ACCOUNT_ID = "1111";

    @Autowired
    private AbstractUserJwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        JUST_GENERATED_TOKEN = jwtUtil.generateToken(ACCOUNT_ID);
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
        assertTrue(jwtUtil.isTokenValidAndContainsAccountId(JUST_GENERATED_TOKEN, ACCOUNT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldHandleTokenWithBearer() {
        assertTrue(jwtUtil.isTokenValidAndContainsAccountId("Bearer " + JUST_GENERATED_TOKEN, ACCOUNT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldReturnFalseForExpiredToken() {
        assertFalse(jwtUtil.isTokenValidAndContainsAccountId(EXPIRED_TOKEN, ACCOUNT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldReturnFalseForInvalidTokens() {
        assertFalse(jwtUtil.isTokenValidAndContainsAccountId(MALFORMED_TOKEN, ACCOUNT_ID));
        assertFalse(jwtUtil.isTokenValidAndContainsAccountId(JUST_GENERATED_TOKEN_CORRUPTED, ACCOUNT_ID));
        assertFalse(jwtUtil.isTokenValidAndContainsAccountId(null, ACCOUNT_ID));
        assertFalse(jwtUtil.isTokenValidAndContainsAccountId("", ACCOUNT_ID));
    }

    @Test
    public void isTokenValidAndContainsAccountIdShouldReturnFalseAnotherUserAccountId() {
        assertFalse(jwtUtil.isTokenValidAndContainsAccountId(JUST_GENERATED_TOKEN, ANOTHER_ACCOUNT_ID));
    }

    @Test
    public void extractExpirationShouldReturnRightTime() {
        long expectedExpirationTime = new Date().getTime() + AbstractUserJwtUtil.EXPIRATION_TIME;
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
        assertEquals(ACCOUNT_ID, jwtUtil.extractAccountId(JUST_GENERATED_TOKEN));
    }

    @Test
    public void extractAccountIdShouldThrowForInvalidOrExpiredTokens() {
        Executable executableExpired = () -> jwtUtil.extractAccountId(EXPIRED_TOKEN);
        assertThrows(Exception.class, executableExpired);

        Executable executableMalformed = () -> jwtUtil.extractAccountId(MALFORMED_TOKEN);
        assertThrows(Exception.class, executableMalformed);

        Executable executableCorrupted = () -> jwtUtil.extractAccountId(JUST_GENERATED_TOKEN_CORRUPTED);
        assertThrows(Exception.class, executableCorrupted);

        Executable executableEmpty = () -> jwtUtil.extractAccountId("");
        assertThrows(Exception.class, executableEmpty);

        Executable executableNull = () -> jwtUtil.extractAccountId(null);
        assertThrows(Exception.class, executableNull);
    }
}
