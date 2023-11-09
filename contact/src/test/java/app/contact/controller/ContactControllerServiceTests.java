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

    private final Contact CONTACT = new Contact(null, Method.SMS, "9999", "name");

    private final Contact DELETE_CONTACT = new Contact(null, Method.SMS, "9999");

    private final static String TOKEN = "token";

    @Mock
    private ContactRepositoryService repositoryService;

    @Mock
    private SecurityJwtWebClient jwtUtil;

    @InjectMocks
    private ContactControllerService controllerService;

    @Test
    public void createShouldCallServiceCreate() {
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
    public void updateShouldCallServiceUpdate() {
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
                "INVALID METHOD",
                CONTACT.getContactId(),
                CONTACT.getNotificationName());

        Executable executable = () -> controllerService.update(TOKEN, request);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void deleteShouldCallServiceDelete() {
        ContactKeyRequest request = new ContactKeyRequest(
                CONTACT.getMethod().name(),
                CONTACT.getContactId());
        controllerService.delete(TOKEN, request);

        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).delete(DELETE_CONTACT);
    }

    @Test
    public void deleteShouldThrowIfInvalidMethod() {
        ContactKeyRequest request = new ContactKeyRequest(
                "INVALID METHOD",
                CONTACT.getContactId());

        Executable executable = () -> controllerService.delete(TOKEN, request);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void clearShouldCallServiceClear() {
        controllerService.clear(TOKEN);


        verify(jwtUtil).extractAccountId(TOKEN);
        verify(repositoryService).clear(null);
    }

    @Test
    public void changeAccountIdShouldCallServiceChangeAccountId() {
        String oldAccountIdToken = TOKEN;
        String newAccountIdToken = "new token";

        ChangeAccountIdRequest changeAccountIdRequest = new ChangeAccountIdRequest(newAccountIdToken);

        controllerService.changeAccountId(oldAccountIdToken, changeAccountIdRequest);

        verify(jwtUtil).extractAccountId(oldAccountIdToken);
        verify(jwtUtil).extractAccountId(newAccountIdToken);

        verify(repositoryService).changeAccountId(null, null);
    }

    @Test
    public void findAllByAccountIdShouldCallServiceFindAllByAccountId() {
        controllerService.findAllByAccountId(TOKEN);

        verify(jwtUtil).extractAccountId(TOKEN);

        verify(repositoryService).findAllByAccountId(null);
    }

    @Test
    public void findAllLikePrimaryKeyShouldCallServiceFindAllLikePrimaryKey() {
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
    public void findAllLikeNotificationNameShouldCallServiceFindAllLikeNotificationName() {
        ContactNNRequest nameRequest = new ContactNNRequest(
                CONTACT.getNotificationName()
        );

        controllerService.findAllLikeNotificationName(TOKEN, nameRequest);

        verify(jwtUtil).extractAccountId(TOKEN);

        verify(repositoryService).findAllLikeNotificationName(null, CONTACT.getNotificationName());
    }
}
