package app.security.config;

import app.security.user.model.User;
import app.security.user.model.UserDetails;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import app.security.user.service.UserDetailsService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    private JwtUtil jwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb");

    //Expired token with "user" username and "pass" password
    private String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjkzMjk3MDMyLCJleHAiOjE2OTMyOTcwMzJ9.wmA063sUsTVGsFSzig9Lr0oiO6C3dxkHH_vWfsbyqlQ";

    @Mock
    private UserDetailsService userDetailsService;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp(){
        jwtAuthFilter = new JwtAuthFilter(jwtUtil, userDetailsService);
    }

    @Test
    void doFilterInternalNormalBehavior() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
            String username = "username";
            String password = "password";
            String token = jwtUtil.generateToken(new UserDetails(new User(username,password)));
            request.addHeader("Authorization","Bearer " + token);
            try {
                jwtAuthFilter.doFilterInternal(request, response, filterChain);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            verify(userDetailsService).loadUserByUsername(username);
    }
    @Test
    void doFilterInternalThrowsExceptionsOnInvalidTokens() {
        //JwtUtil with another sing key
        JwtUtil invalidJwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9aa");
        String username = "username";
        String password = "password";
        String token = jwtUtil.generateToken(new UserDetails(new User(username,password)));
        String invalidToken = invalidJwtUtil.generateToken(new UserDetails(new User(username,password)));
        List<String> invalidList = new ArrayList<>();
        invalidList.add("Bearer " + EXPIRED_TOKEN);
        invalidList.add("Bearer " + invalidToken);
        invalidList.add("Bearer " + token.substring(0,token.length()-1));
        invalidList.add("Bearer " + token.substring(1));
        invalidList.add("Bearer " + token.substring(token.indexOf('.')));
        invalidList.add(token);
        invalidList.add("Random " + token);
        for(String invalid : invalidList){
            response = new MockHttpServletResponse();
            request = new MockHttpServletRequest();
            request.addHeader("Authorization",invalid);
            try {
                jwtAuthFilter.doFilterInternal(request, response, filterChain);
                verify(filterChain).doFilter(request,response);
                verifyNoInteractions(userDetailsService);
                reset(filterChain);
                reset(userDetailsService);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        request.addHeader("InvalidHeader",token);
        try {
            jwtAuthFilter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request,response);
            verifyNoInteractions(userDetailsService);
            reset(filterChain);
            reset(userDetailsService);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}