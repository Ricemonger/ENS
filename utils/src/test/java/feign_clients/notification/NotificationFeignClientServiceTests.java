package feign_clients.notification;

import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignClient;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import app.utils.feign_clients.notification.dto.NotificationNameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationFeignClientServiceTests {

    private final static String TOKEN = "TOKEN";

    private final static String NAME = "NAME";

    private final static String TEXT = "TEXT";

    private final static Notification NOTIFICATION = new Notification(NAME, TEXT);

    private final static NotificationNameRequest REQUEST = new NotificationNameRequest(NAME);

    private final static List<Notification> NOTIFICATIONS = new ArrayList<>();

    static {
        NOTIFICATIONS.add(new Notification(NAME, TEXT));
        NOTIFICATIONS.add(new Notification("NAME2", "TEXT2"));
        NOTIFICATIONS.add(new Notification("NAME3", "TEXT3"));
    }

    @Spy
    private MockNotificationFeignClient notificationFeignClient;

    @InjectMocks
    private NotificationFeignClientService notificationFeignClientService;

    @BeforeEach
    public void setUp() {
        notificationFeignClientService = new NotificationFeignClientService(notificationFeignClient);
    }

    @Test
    public void findOneByPrimaryKey() {
        Notification result = notificationFeignClientService.findOneByPrimaryKey(TOKEN, NAME);

        verify(notificationFeignClient).findAllByPrimaryKey(TOKEN, REQUEST);

        assertEquals(new Notification(NAME, TEXT), result);
    }

    @Test
    public void getMapByAccountId() {
        Map<String, String> result = notificationFeignClientService.getMapByAccountId(TOKEN);

        verify(notificationFeignClient).findAllByAccountId(TOKEN);

        Map<String, String> expected = new HashMap<>();
        for (Notification notification : NOTIFICATIONS) {
            expected.put(notification.getName(), notification.getText());
        }

        assertEquals(expected, result);
    }

    @Test
    public void findAllById() {
        List<Notification> result = notificationFeignClientService.findAllById(TOKEN);

        verify(notificationFeignClient).findAllByAccountId(TOKEN);

        assertEquals(notificationFeignClient.findAllByAccountId(TOKEN), result);
    }

    @Test
    public void addOne() {
        notificationFeignClientService.addOne(TOKEN, NOTIFICATION);

        verify(notificationFeignClient).create(TOKEN, NOTIFICATION);
    }

    @Test
    public void removeMany() {
        notificationFeignClientService.removeMany(TOKEN, NOTIFICATION);

        verify(notificationFeignClient).findAllByAccountId(TOKEN);

        for (Notification notification : NOTIFICATIONS) {
            verify(notificationFeignClient).delete(TOKEN, new NotificationNameRequest(notification.getName()));
        }
    }

    @Test
    public void removeOne() {
        notificationFeignClientService.removeOne(TOKEN, NOTIFICATION);

        verify(notificationFeignClient).delete(TOKEN, REQUEST);
    }

    @Test
    public void removeAllById() {
        notificationFeignClientService.removeAllById(TOKEN);

        verify(notificationFeignClient).clear(TOKEN);
    }

    @Test
    public void changeAccountId() {
        String oldToken = "OLD";
        String newToken = "NEW";

        notificationFeignClientService.changeAccountId(oldToken, newToken);

        verify(notificationFeignClient).changeAccountId(oldToken, new ChangeAccountIdRequest(newToken));
    }

    private static class MockNotificationFeignClient implements NotificationFeignClient {

        @Override
        public Notification create(String securityToken, Notification request) {
            return request;
        }

        @Override
        public Notification update(String securityToken, Notification request) {
            return request;
        }

        @Override
        public Notification delete(String securityToken, NotificationNameRequest request) {
            return new Notification(request.name(), securityToken);
        }

        @Override
        public void clear(String securityToken) {
        }

        @Override
        public void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        }

        @Override
        public List<Notification> findAllByAccountId(String securityToken) {
            return NOTIFICATIONS;
        }

        @Override
        public List<Notification> findAllByPrimaryKey(String securityToken, NotificationNameRequest request) {
            return NOTIFICATIONS.stream().filter(s -> s.getName().startsWith(request.name())).toList();
        }
    }
}

