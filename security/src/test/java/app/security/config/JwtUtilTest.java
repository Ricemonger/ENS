package app.security.config;

import app.security.security.JwtUtil;
import app.security.user.service.ens_user.EnsUser;
import app.security.user.service.ens_user.EnsUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb");

    //Expired token with "user" username and "pass" password with b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb key
    private final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjkzMjk3MDMyLCJleHAiOjE2OTMyOTcwMzJ9.wmA063sUsTVGsFSzig9Lr0oiO6C3dxkHH_vWfsbyqlQ";

    private final EnsUserDetails ensUserDetails = new EnsUserDetails(new EnsUser("user", "pass"));
    private final String token = jwtUtil.generateToken(ensUserDetails);

    @Test
    void generateToken() {
        String regex = "(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)";
        System.out.println(token);
        assertTrue(token.matches(regex));
    }


    @Test
    void isTokenValid() {
        assertTrue(jwtUtil.isTokenValid(token, ensUserDetails));
        String username1 = "user1";
        String password1 = "pass1";
        EnsUserDetails ensUserDetails1 = new EnsUserDetails(new EnsUser(username1, password1));
        String token1 = jwtUtil.generateToken(ensUserDetails1);
        assertFalse(jwtUtil.isTokenValid(token1, ensUserDetails));
        assertFalse(jwtUtil.isTokenValid(token, ensUserDetails1));
        assertFalse(jwtUtil.isTokenValid(EXPIRED_TOKEN, ensUserDetails));
    }

    @Test
    void isTokenExpired() {
        assertFalse(jwtUtil.isTokenExpired(token));
        assertTrue(jwtUtil.isTokenExpired(EXPIRED_TOKEN));
    }

    @Test
    void extractExpiration() {
        long time = System.currentTimeMillis() + 3_600_000;
        long expiation = jwtUtil.extractExpiration(token).getTime();
        boolean condition = Math.abs(time - expiation) <= 10_000;
        assertTrue(condition);
    }

    @Test
    void extractUsername() {
        assertEquals("user", jwtUtil.extractAccountId(token));
    }

    @Test
    void extractClaimThrowsJwtExceptions() {
        JwtUtil invalidJwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9yt");
        List<String> invalidTokens = new ArrayList<>();
        invalidTokens.add(EXPIRED_TOKEN);
        invalidTokens.add(token.substring(0, token.length() - 5));
        invalidTokens.add(token.substring(5));
        invalidTokens.add(invalidJwtUtil.generateToken(ensUserDetails));
        invalidTokens.add(token.substring(token.indexOf(".") + 1));
        for (String invalidToken : invalidTokens) {
            Executable executableExpired = () -> jwtUtil.extractClaim(invalidToken, Claims::getSubject);
            assertThrows(JwtException.class, executableExpired);
        }
    }
}