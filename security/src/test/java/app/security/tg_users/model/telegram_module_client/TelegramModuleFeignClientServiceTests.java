package app.security.tg_users.model.telegram_module_client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramModuleFeignClientServiceTests {

    private final static String TOKEN = "TOKEN";

    @Mock
    private TelegramModuleFeignClient feignClient;

    @InjectMocks
    private TelegramModuleFeignClientService service;

    @Test
    public void getChatId() {
        service.getChatId(TOKEN);

        verify(feignClient).getChatId(TOKEN);
    }
}
