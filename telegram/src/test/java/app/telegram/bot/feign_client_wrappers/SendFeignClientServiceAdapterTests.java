package app.telegram.bot.feign_client_wrappers;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.sender.SendFeignClientService;
import app.utils.feign_clients.sender.dto.SendManyRequest;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SendFeignClientServiceAdapterTests {

    private final static Long CHAT_ID = 111L;

    private final static String TOKEN = "TOKEN";

    @Mock
    private SendFeignClientService sendFeignClientService;

    @Mock
    private TelegramUserService telegramUserService;

    @InjectMocks
    private SendFeignClientServiceAdapter sendAdapter;

    @BeforeEach
    public void setUp() {
        when(telegramUserService.findSecurityTokenOrGenerateAndPut(CHAT_ID)).thenReturn(TOKEN);
    }

    @Test
    public void sendOne() {
        SendOneRequest request = new SendOneRequest("method", "contactId", "notificationName");

        sendAdapter.sendOne(CHAT_ID, request);

        verify(sendFeignClientService).sendOne(TOKEN, request);
    }

    @Test
    public void sendMany() {
        SendManyRequest request = new SendManyRequest("method", "contactId", "notificationText");

        sendAdapter.sendMany(CHAT_ID, request);

        verify(sendFeignClientService).sendMany(TOKEN, request);
    }

    @Test
    public void sendAll() {
        sendAdapter.sendAll(CHAT_ID);

        verify(sendFeignClientService).sendAll(TOKEN);
    }
}
