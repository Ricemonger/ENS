package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.utils.feign_clients.ChangeAccountIdRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTests {

    private final static String securityToken = "token";

    private final static ContactCreUpdRequest creUpdRequest = new ContactCreUpdRequest("sms", "999", "999");

    private final static ContactKeyRequest keyRequest = new ContactKeyRequest("sms", "999");

    private final static ChangeAccountIdRequest changeAccountIdRequest = new ChangeAccountIdRequest("new token");

    private final static ContactNNRequest nnRequest = new ContactNNRequest("111");

    @Mock
    private ContactControllerService service;

    @InjectMocks
    private ContactController controller;

    @Test
    public void create() {
        controller.create(securityToken, creUpdRequest);
        verify(service).create(securityToken, creUpdRequest);
    }

    @Test
    public void update() {
        controller.update(securityToken, creUpdRequest);
        verify(service).update(securityToken, creUpdRequest);
    }

    @Test
    public void delete() {
        controller.delete(securityToken, keyRequest);
        verify(service).delete(securityToken, keyRequest);
    }

    @Test
    public void clear() {
        controller.clear(securityToken);
        verify(service).clear(securityToken);
    }

    @Test
    public void changeAccountId() {
        controller.changeAccountId(securityToken, changeAccountIdRequest);
        verify(service).changeAccountId(securityToken, changeAccountIdRequest);
    }

    @Test
    public void findAllByAccountIdUN() {
        controller.findAllByAccountIdUN(securityToken);
        verify(service).findAllByAccountId(securityToken);
    }

    @Test
    public void findAllByAccountId() {
        controller.findAllByAccountId(securityToken);
        verify(service).findAllByAccountId(securityToken);
    }

    @Test
    public void findAllLikePrimaryKey() {
        controller.findAllLikePrimaryKey(securityToken, keyRequest);
        verify(service).findAllLikePrimaryKey(securityToken, keyRequest);
    }

    @Test
    public void findAllLikeNotificationName() {
        controller.findAllLikeNotificationName(securityToken, nnRequest);
        verify(service).findAllLikeNotificationName(securityToken, nnRequest);
    }
}
