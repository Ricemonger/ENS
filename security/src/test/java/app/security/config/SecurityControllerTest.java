package app.security.config;

import app.security.security.JwtUtil;
import app.security.security.SecurityController;
import app.security.user.service.ens_user.EnsUser;
import app.security.user.service.ens_user.EnsUserDetails;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityControllerTest {

    private final JwtUtil jwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb");

    //Expired token with "user" username and "pass" password with b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb key
    private final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjkzMjk3MDMyLCJleHAiOjE2OTMyOTcwMzJ9.wmA063sUsTVGsFSzig9Lr0oiO6C3dxkHH_vWfsbyqlQ";

    private SecurityController securityController;

    @BeforeEach
    void setUp() {
        securityController = new SecurityController(jwtUtil);
    }

    @Test
    void getUsernameNormalBehavior() {
        String username = "username";
        String password = "password";
        EnsUserDetails ensUserDetails = new EnsUserDetails(new EnsUser(username, password));
        String token = jwtUtil.generateToken(ensUserDetails);
        assertEquals(jwtUtil.extractAccountId(token), securityController.getUsername(token));
    }

    @Test
    void getUsernameThrowsExceptionOnInvalidTokens() {
        String username = "username";
        String password = "password";
        EnsUserDetails ensUserDetails = new EnsUserDetails(new EnsUser(username, password));
        String validToken = jwtUtil.generateToken(ensUserDetails);
        List<String> invalidTokens = new ArrayList<>();
        invalidTokens.add(EXPIRED_TOKEN);
        invalidTokens.add(validToken.substring(1));
        invalidTokens.add(validToken.substring(0, validToken.length() - 1));
        invalidTokens.add(validToken.substring(validToken.indexOf('.')));
        for (String invalidToken : invalidTokens) {
            Executable executable = () -> securityController.getUsername(invalidToken);
            assertThrows(JwtException.class, executable);
        }
    }
}