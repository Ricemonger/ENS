package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.utils.feign_clients.ChangeAccountIdRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTests {

    private final static String TOKEN = "token";

    private final static NotificationCreUpdRequest CRE_UPD_REQUEST = new NotificationCreUpdRequest("name", "text");

    private final static NotificationNameRequest NAME_REQUEST = new NotificationNameRequest("name");

    private final static ChangeAccountIdRequest CHANGE_ACCOUNT_ID_REQUEST = new ChangeAccountIdRequest("new token");

    @Mock
    private NotificationControllerService service;

    @InjectMocks
    private NotificationController controller;

    @Test
    public void create() {
        controller.create(TOKEN, CRE_UPD_REQUEST);
        verify(service).create(TOKEN, CRE_UPD_REQUEST);
    }

    @Test
    public void update() {
        controller.update(TOKEN, CRE_UPD_REQUEST);
        verify(service).update(TOKEN, CRE_UPD_REQUEST);
    }

    @Test
    public void delete() {
        controller.delete(TOKEN, NAME_REQUEST);
        verify(service).delete(TOKEN, NAME_REQUEST);
    }

    @Test
    public void clear() {
        controller.clear(TOKEN);
        verify(service).clear(TOKEN);
    }

    @Test
    public void changeAccountId() {
        controller.changeAccountId(TOKEN, CHANGE_ACCOUNT_ID_REQUEST);
        verify(service).changeAccountId(TOKEN, CHANGE_ACCOUNT_ID_REQUEST);
    }

    @Test
    public void findAllByAccountIdUN() {
        controller.findAllByAccountIdUN(TOKEN);
        verify(service).findAllByAccountId(TOKEN);
    }

    @Test
    public void findAllByAccountId() {
        controller.findAllByAccountId(TOKEN);
        verify(service).findAllByAccountId(TOKEN);
    }

    @Test
    public void findAllByPrimaryKey() {
        controller.findAllLikePrimaryKey(TOKEN, NAME_REQUEST);
        verify(service).findAllLikePrimaryKey(TOKEN, NAME_REQUEST);
    }
}
