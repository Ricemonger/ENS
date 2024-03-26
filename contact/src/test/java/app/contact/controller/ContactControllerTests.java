package app.contact.controller;

import app.utils.services.contact.dto.ContactCreUpdRequest;
import app.utils.services.contact.dto.ContactKeyRequest;
import app.utils.services.contact.dto.ContactNNRequest;
import app.utils.services.telegram.dto.ChangeAccountIdRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTests {

    private final static String TOKEN = "token";

    private final static ContactCreUpdRequest CRE_UPD_REQUEST = new ContactCreUpdRequest("sms", "999", "999");

    private final static ContactKeyRequest PRIMARY_KEY_REQUEST = new ContactKeyRequest("sms", "999");

    private final static ContactNNRequest NOTIFICATION_NAME_REQUEST = new ContactNNRequest("111");

    private final static ChangeAccountIdRequest CHANGE_ACCOUNT_ID_REQUEST = new ChangeAccountIdRequest("new token");

    @Mock
    private ContactControllerService service;

    @InjectMocks
    private ContactController controller;

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
        controller.delete(TOKEN, PRIMARY_KEY_REQUEST);
        verify(service).delete(TOKEN, PRIMARY_KEY_REQUEST);
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
    public void findAllLikePrimaryKey() {
        controller.findAllLikePrimaryKey(TOKEN, PRIMARY_KEY_REQUEST);
        verify(service).findAllLikePrimaryKey(TOKEN, PRIMARY_KEY_REQUEST);
    }

    @Test
    public void findAllLikeNotificationName() {
        controller.findAllLikeNotificationName(TOKEN, NOTIFICATION_NAME_REQUEST);
        verify(service).findAllLikeNotificationName(TOKEN, NOTIFICATION_NAME_REQUEST);
    }
}
