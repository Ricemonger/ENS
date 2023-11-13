package app.security.ens_users.model.security;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.EnsUser;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
public class EnsUserJwtAuthFilterTests {

    private final static String INVALID_TOKEN = "INVALID TOKEN";

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private MockHttpServletResponse response = new MockHttpServletResponse();

    @MockBean
    private FilterChain filterChain;

    @SpyBean
    private AbstractUserJwtUtil abstractUserJwtUtil;

    @SpyBean
    private EnsUserDetailsService ensUserDetailsService;

    @Autowired
    private EnsUserJwtAuthFilter ensUserJwtAuthFilter;

    @Autowired
    private EnsUserRepositoryService repositoryService;

    @BeforeEach
    public void setUp() {
        repositoryService.deleteAll();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void doFilterInternalShouldNotAuthorizeIfNullToken() {
        try {
            ensUserJwtAuthFilter.doFilterInternal(request, response, filterChain);

            verifyNoInteractions(abstractUserJwtUtil, ensUserDetailsService);
            verify(filterChain).doFilter(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doFilterInternalShouldExtractTokenAndNotAuthorizeIfInvalid() throws ServletException, IOException {
        request.addHeader("Authorization", INVALID_TOKEN);

        ensUserJwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(abstractUserJwtUtil).extractAccountId(INVALID_TOKEN);

        verifyNoInteractions(ensUserDetailsService);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterInternalShouldExtractTokenAndAuthorizeIfValid() throws ServletException, IOException {
        String accountId = repositoryService.save(new EnsUser("username", "password")).getAccountId();
        String generatedToken = abstractUserJwtUtil.generateToken(accountId);
        request.addHeader("Authorization", generatedToken);

        ensUserJwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(abstractUserJwtUtil, times(2)).extractAccountId(generatedToken);

        verify(ensUserDetailsService).loadUserByAccountId(accountId);

        verify(abstractUserJwtUtil).isTokenValidAndContainsAccountId(generatedToken,
                accountId);

        verify(filterChain).doFilter(request, response);
    }

}
