package feign_clients.security;

import app.utils.feign_clients.security_abstract.SecurityFeignClient;
import app.utils.feign_clients.security_abstract.SecurityFeignClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SecurityFeignClientServiceTests {

    private final static String TOKEN = "TOKEN";

    private final static String MOCK_STRING = "MOCK_STRING";

    @Spy
    private MockSecurityFeignClient securityFeignClient;

    @InjectMocks
    private SecurityFeignClientService securityFeignClientService;

    @Test
    public void getAccountId() {
        String result = securityFeignClientService.extractAccountId(TOKEN);

        verify(securityFeignClient).getAccountId(TOKEN);

        assertEquals(MOCK_STRING, result);
    }

    private static class MockSecurityFeignClient implements SecurityFeignClient {

        @Override
        public String getAccountId(String securityToken) {
            return MOCK_STRING;
        }
    }

}
