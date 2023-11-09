package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.exceptions.InvalidContactMethodException;
import app.contact.model.Contact;
import app.contact.model.Method;
import app.contact.model.db.ContactRepositoryService;
import app.utils.SecurityJwtWebClient;
import app.utils.feign_clients.ChangeAccountIdRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContactControllerServiceTests {

    private final static String TOKEN = "token";

    private final static String ANOTHER_TOKEN = "another token";

    private final static String INVALID_METHOD = "invalid method";

    private final static Contact CONTACT = new Contact(null, Method.SMS, "9999", "name");

    private final static Contact DELETE_CONTACT = new Contact(null, Method.SMS, "9999");

    @Mock
    private ContactRepositoryService repositoryService;

    @Mock
    private SecurityJwtWebClient jwtUtil;

    @InjectMocks
    private ContactControllerService controllerService;

    @Test
    public void create() {
        ContactCreUpdRequest request = new ContactCreUpdRequest(
                CONTACT.getMethod().name(),
                CONTACT.getContactId(),
                CONTACT.getNotificationName());

        controllerService.create(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).create(CONTACT);
    }

    @Test
    public void createShouldThrowIfInvalidMethod() {
        ContactCreUpdRequest request = new ContactCreUpdRequest(
                "INVALID METHOD",
                CONTACT.getContactId(),
                CONTACT.getNotificationName());

        Executable executable = () -> controllerService.create(TOKEN, request);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void update() {
        ContactCreUpdRequest request = new ContactCreUpdRequest(
                CONTACT.getMethod().name(),
                CONTACT.getContactId(),
                CONTACT.getNotificationName());

        controllerService.update(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).update(CONTACT);
    }

    @Test
    public void updateShouldThrowIfInvalidMethod() {
        ContactCreUpdRequest request = new ContactCreUpdRequest(
                INVALID_METHOD,
                CONTACT.getContactId(),
                CONTACT.getNotificationName());

        Executable executable = () -> controllerService.update(TOKEN, request);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void delete() {
        ContactKeyRequest request = new ContactKeyRequest(
                DELETE_CONTACT.getMethod().name(),
                DELETE_CONTACT.getContactId());

        controllerService.delete(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).delete(DELETE_CONTACT);
    }

    @Test
    public void deleteShouldThrowIfInvalidMethod() {
        ContactKeyRequest request = new ContactKeyRequest(
                INVALID_METHOD,
                DELETE_CONTACT.getContactId());

        Executable executable = () -> controllerService.delete(TOKEN, request);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void clear() {
        controllerService.clear(TOKEN);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).clear(null);
    }

    @Test
    public void changeAccountId() {
        ChangeAccountIdRequest changeAccountIdRequest = new ChangeAccountIdRequest(ANOTHER_TOKEN);

        controllerService.changeAccountId(TOKEN, changeAccountIdRequest);

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
        ContactKeyRequest keyRequest = new ContactKeyRequest(
                CONTACT.getMethod().name(),
                CONTACT.getContactId()
        );

        controllerService.findAllLikePrimaryKey(TOKEN, keyRequest);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).findAllLikePrimaryKey(null, CONTACT.getMethod(), CONTACT.getContactId());
    }

    @Test
    public void findAllLikePrimaryKeyShouldThrowIfInvalidMethod() {
        ContactKeyRequest keyRequest = new ContactKeyRequest(
                "INVALID METHOD",
                CONTACT.getContactId()
        );

        Executable executable = () -> controllerService.findAllLikePrimaryKey(TOKEN, keyRequest);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void findAllLikeNotificationName() {
        ContactNNRequest nameRequest = new ContactNNRequest(
                CONTACT.getNotificationName()
        );

        controllerService.findAllLikeNotificationName(TOKEN, nameRequest);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).findAllLikeNotificationName(null, CONTACT.getNotificationName());
    }
}
