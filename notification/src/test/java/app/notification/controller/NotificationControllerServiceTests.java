package app.notification.controller;

import app.notification.model.Notification;
import app.notification.model.NotificationService;
import app.utils.services.notification.dto.NotificationCreUpdRequest;
import app.utils.services.notification.dto.NotificationNameRequest;
import app.utils.services.security.abstact.feign.SecurityFeignClientService;
import app.utils.services.telegram.dto.ChangeAccountIdRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerServiceTests {

    private final static String TOKEN = "token";

    private final static String ANOTHER_TOKEN = "another token";

    private final static Notification NOTIFICATION = new Notification(null, "name", "text");

    private final static Notification DELETE_NOTIFICATION = new Notification(null, "name");

    @Mock
    private NotificationService notificationService;

    @Mock
    private SecurityFeignClientService jwtUtil;

    @InjectMocks
    private NotificationControllerService controllerService;

    @Test
    public void create() {
        NotificationCreUpdRequest request = new NotificationCreUpdRequest(
                NOTIFICATION.getName(),
                NOTIFICATION.getText());

        controllerService.create(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(notificationService).create(NOTIFICATION);
    }

    @Test
    public void update() {
        NotificationCreUpdRequest request = new NotificationCreUpdRequest(
                NOTIFICATION.getName(),
                NOTIFICATION.getText());

        controllerService.update(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(notificationService).update(NOTIFICATION);
    }

    @Test
    public void delete() {
        NotificationNameRequest request = new NotificationNameRequest(DELETE_NOTIFICATION.getName());

        controllerService.delete(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(notificationService).delete(DELETE_NOTIFICATION);
    }

    @Test
    public void clear() {
        controllerService.clear(TOKEN);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(notificationService).clear(null);
    }

    @Test
    public void changeAccountId() {
        ChangeAccountIdRequest request = new ChangeAccountIdRequest(ANOTHER_TOKEN);

        controllerService.changeAccountId(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(jwtUtil).extractAccountId(ANOTHER_TOKEN);
        verify(notificationService).changeAccountId(null, null);
    }

    @Test
    public void findAllByAccountId() {
        controllerService.findAllByAccountId(TOKEN);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(notificationService).findAllByAccountId(null);
    }

    @Test
    public void findAllLikePrimaryKey() {
        NotificationNameRequest request = new NotificationNameRequest(NOTIFICATION.getName());

        controllerService.findAllLikePrimaryKey(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(notificationService).findAllLikePrimaryKey(null, NOTIFICATION.getName());
    }
}
