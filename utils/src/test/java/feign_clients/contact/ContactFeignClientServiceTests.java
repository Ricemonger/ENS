package feign_clients.contact;

import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.contact.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContactFeignClientServiceTests {

    private final static String TOKEN = "TOKEN";

    private final static Method METHOD = Method.SMS;

    private final static String CONTACT_ID = "CONTACT_ID";

    private final static String NOTIFICATION_NAME = "NOTIFICATION_NAME";

    private final static Contact CONTACT = new Contact(METHOD, CONTACT_ID, NOTIFICATION_NAME);

    private final static ContactPKRequest REQUEST = new ContactPKRequest(METHOD.name(), CONTACT_ID);

    private final static List<Contact> CONTACTS = new ArrayList<>();

    static {
        CONTACTS.add(CONTACT);
        CONTACTS.add(new Contact(Method.VIBER, CONTACT_ID, NOTIFICATION_NAME));
        CONTACTS.add(new Contact(Method.EMAIL, CONTACT_ID, NOTIFICATION_NAME));
    }

    @Spy
    private MockContactFeignClient contactFeignClient;

    @InjectMocks
    private ContactFeignClientService contactFeignClientService;

    @BeforeEach
    public void setUp() {
        contactFeignClientService = new ContactFeignClientService(contactFeignClient);
    }

    @Test
    public void findOneByPrimaryKey() {
        Contact result = contactFeignClientService.findOneByPrimaryKey(TOKEN, METHOD.name(), CONTACT_ID);

        verify(contactFeignClient).findAllLikePrimaryKey(TOKEN, REQUEST);

        assertEquals(CONTACT, result);
    }

    @Test
    public void findAllById() {
        List<Contact> result = contactFeignClientService.findAllById(TOKEN);

        verify(contactFeignClient).findAllByAccountId(TOKEN);

        assertEquals(contactFeignClient.findAllByAccountId(TOKEN), result);
    }

    @Test
    public void addOne() {
        contactFeignClientService.addOne(TOKEN, CONTACT);

        verify(contactFeignClient).create(TOKEN, CONTACT);
    }

    @Test
    public void removeMany() {
        contactFeignClientService.removeMany(TOKEN, CONTACTS);

        for (Contact contact : CONTACTS) {
            verify(contactFeignClient).delete(TOKEN, contact);
        }
    }

    @Test
    public void removeOne() {
        contactFeignClientService.removeOne(TOKEN, CONTACT);

        verify(contactFeignClient).delete(TOKEN, CONTACT);
    }

    @Test
    public void removeAllById() {
        contactFeignClientService.removeAllById(TOKEN);

        verify(contactFeignClient).clear(TOKEN);
    }

    @Test
    public void changeAccountId() {
        String oldToken = "OLD";
        String newToken = "NEW";

        contactFeignClientService.changeAccountId(oldToken, newToken);

        verify(contactFeignClient).changeAccountId(oldToken, new ChangeAccountIdRequest(newToken));
    }

    private static class MockContactFeignClient implements ContactFeignClient {


        @Override
        public Contact create(String token, Contact request) {
            return request;
        }

        @Override
        public Contact update(String token, Contact request) {
            return request;
        }

        @Override
        public Contact delete(String token, Contact request) {
            return request;
        }

        @Override
        public void clear(String token) {
        }

        @Override
        public void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        }

        @Override
        public List<Contact> findAllByAccountId(String token) {
            return CONTACTS;
        }

        @Override
        public List<Contact> findAllLikePrimaryKey(String token, ContactPKRequest request) {
            return CONTACTS.stream().filter(s -> (s.getMethod().name().equals(request.method()) && s.getContactId().startsWith(request.contactId()))).toList();
        }
    }
}
