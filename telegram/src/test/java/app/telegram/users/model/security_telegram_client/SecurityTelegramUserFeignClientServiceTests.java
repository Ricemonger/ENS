package app.telegram.users.model.security_telegram_client;

import app.utils.feign_clients.security_telegram.SecurityTelegramUserFeignClient;
import app.utils.feign_clients.security_telegram.SecurityTelegramUserFeignClientService;
import app.utils.feign_clients.security_telegram.dto.UsernamePasswordRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SecurityTelegramUserFeignClientServiceTests {

    private final static String TOKEN = "TOKEN";

    private final static boolean MOCK_BOOLEAN = false;

    private final static String MOCK_STRING = "MOCK_STRING";

    @Spy
    private MockSecurityTelegramUserFeignClient feignClient;

    @InjectMocks
    private SecurityTelegramUserFeignClientService service;

    @Test
    public void create() {
        String result = service.create(TOKEN);

        verify(feignClient).create(TOKEN);

        assertEquals(MOCK_STRING, result);
    }

    @Test
    public void delete() {
        service.delete(TOKEN);

        verify(feignClient).delete(TOKEN);
    }

    @Test
    public void link() {
        String username = "username";
        String password = "password";
        UsernamePasswordRequest request = new UsernamePasswordRequest(username, password);

        service.link(TOKEN, username, password);

        verify(feignClient).link(TOKEN, request);
    }

    @Test
    public void unlink() {
        service.unlink(TOKEN);

        verify(feignClient).unlink(TOKEN);
    }

    @Test
    public void isLinked() {
        boolean result = service.isLinked(TOKEN);

        verify(feignClient).isLinked(TOKEN);

        assertEquals(MOCK_BOOLEAN, result);
    }

    @Test
    public void getSecurityToken() {
        String result = service.getSecurityToken(TOKEN);

        verify(feignClient).getSecurityToken(TOKEN);

        assertEquals(MOCK_STRING, result);
    }

    @Test
    public void getAccountInfo() {
        String result = service.getAccountInfo(TOKEN);

        verify(feignClient).getAccountInfo(TOKEN);

        assertEquals(MOCK_STRING, result);
    }

    @Test
    public void doesUserExist() {
        boolean result = service.doesUserExists(TOKEN);

        verify(feignClient).doesUserExists(TOKEN);

        assertEquals(MOCK_BOOLEAN, result);
    }

    private static class MockSecurityTelegramUserFeignClient implements SecurityTelegramUserFeignClient {

        @Override
        public boolean doesUserExists(String telegramToken) {
            return MOCK_BOOLEAN;
        }

        @Override
        public String create(String telegramToken) {
            return MOCK_STRING;
        }

        @Override
        public void delete(String telegramToken) {
        }

        @Override
        public void link(String telegramToken, UsernamePasswordRequest request) {
        }

        @Override
        public void unlink(String telegramToken) {
        }

        @Override
        public boolean isLinked(String telegramToken) {
            return MOCK_BOOLEAN;
        }

        @Override
        public String getSecurityToken(String telegramToken) {
            return MOCK_STRING;
        }

        @Override
        public String getAccountInfo(String telegramToken) {
            return MOCK_STRING;
        }
    }

}
