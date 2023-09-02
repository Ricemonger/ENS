package app.send.service.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactClient contactClient;

    private ContactService contactService;

    @BeforeEach
    void setUp(){
        contactService = new ContactService(contactClient);
    }

    @Test
    void findOneByPrimaryKeyNormalBehavior() {
        String token = "token";
        String method = "method";
        String id = "contactId";
        ContactPKRequest request = new ContactPKRequest(method,id);
        try {
            contactService.findOneByPrimaryKey(token,method,id);
        }catch (Exception e){}
        verify(contactClient).findAllLikePrimaryKey(token,request);
    }
    @Test
    void findOneByPrimaryKeyThrowsExceptionOnInvalidMethod() {
        String token = "token";
        String method = "method";
        String id = "contactId";
        ContactPKRequest request = new ContactPKRequest(method,id);
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                try {
                    contactService.findOneByPrimaryKey(token,method,id);
                }catch (NoSuchElementException e){}
            }
        };
        assertThrows(IllegalArgumentException.class,executable);
    }

    @Test
    void findAllByUsername() {
        String token = "token";
        contactService.findAllByUsername(token);
        verify(contactClient).findAllByUsername(token);
    }
}