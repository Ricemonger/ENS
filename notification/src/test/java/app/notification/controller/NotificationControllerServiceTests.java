package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.model.Notification;
import app.notification.model.db.NotificationRepositoryService;
import app.utils.SecurityJwtWebClient;
import app.utils.feign_clients.ChangeAccountIdRequest;
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
    private NotificationRepositoryService repositoryService;

    @Mock
    private SecurityJwtWebClient jwtUtil;

    @InjectMocks
    private NotificationControllerService controllerService;

    @Test
    public void create() {
        NotificationCreUpdRequest request = new NotificationCreUpdRequest(
                NOTIFICATION.getName(),
                NOTIFICATION.getText());

        controllerService.create(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).create(NOTIFICATION);
    }

    @Test
    public void update() {
        NotificationCreUpdRequest request = new NotificationCreUpdRequest(
                NOTIFICATION.getName(),
                NOTIFICATION.getText());

        controllerService.update(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).update(NOTIFICATION);
    }

    @Test
    public void delete() {
        NotificationNameRequest request = new NotificationNameRequest(DELETE_NOTIFICATION.getName());

        controllerService.delete(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).delete(DELETE_NOTIFICATION);
    }

    @Test
    public void clear() {
        controllerService.clear(TOKEN);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).clear(null);
    }

    @Test
    public void changeAccountId() {
        ChangeAccountIdRequest request = new ChangeAccountIdRequest(ANOTHER_TOKEN);

        controllerService.changeAccountId(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(jwtUtil).extractAccountId(ANOTHER_TOKEN);
        verify(repositoryService).changeAccountId(null, null);
    }

    @Test
    public void findAllByAccountId() {
        controllerService.findAllByAccountId(TOKEN);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).findAllByAccountId(null);
    }

    @Test
    public void findAllLikePrimaryKey() {
        NotificationNameRequest request = new NotificationNameRequest(NOTIFICATION.getName());

        controllerService.findAllLikePrimaryKey(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).findAllLikePrimaryKey(null, NOTIFICATION.getName());
    }
}
