package app.telegram.bot.feign_client_adapters;

import app.telegram.users.controller.TelegramUserService;
import app.utils.services.notification.Notification;
import app.utils.services.notification.feign.NotificationFeignClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationFeignClientServiceAdapterTests {

    private final static Long CHAT_ID = 111L;

    private final static String TOKEN = "TOKEN";

    private final static Notification NOTIFICATION = new Notification("NAME", "TEXT");

    @Mock
    private NotificationFeignClientService notificationFeignClientService;

    @Mock
    private TelegramUserService telegramUserService;

    @InjectMocks
    private NotificationFeignClientServiceAdapter notificationAdapter;

    @BeforeEach
    public void setUp() {
        when(telegramUserService.findSecurityTokenOrGenerateAndPut(CHAT_ID)).thenReturn(TOKEN);
    }

    @Test
    public void findAll() {
        notificationAdapter.findAll(CHAT_ID);

        verify(notificationFeignClientService).findAllById(TOKEN);
    }

    @Test
    public void addOne() {
        notificationAdapter.addOne(CHAT_ID, NOTIFICATION);

        verify(notificationFeignClientService).addOne(TOKEN, NOTIFICATION);
    }

    @Test
    public void removeOne() {
        notificationAdapter.removeOne(CHAT_ID, NOTIFICATION);

        verify(notificationFeignClientService).removeOne(TOKEN, NOTIFICATION);
    }

    @Test
    public void removeMany() {
        notificationAdapter.removeMany(CHAT_ID, NOTIFICATION);

        verify(notificationFeignClientService).removeMany(TOKEN, NOTIFICATION);
    }

    @Test
    public void clear() {
        notificationAdapter.clear(CHAT_ID);

        verify(notificationFeignClientService).removeAllById(TOKEN);
    }
}
