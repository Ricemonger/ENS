package app.telegram.bot.feign_client_wrappers;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.contact.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactFeignClientServiceAdapterTests {

    private final static Long CHAT_ID = 111L;

    private final static String TOKEN = "TOKEN";

    private final static Contact CONTACT = new Contact(Method.SMS, "contactId", "notificationName");

    private final static List<Contact> MOCK_LIST = new ArrayList<>();

    @Mock
    private ContactFeignClientService contactFeignClientService;

    @Mock
    private TelegramUserService telegramUserService;

    @InjectMocks
    private ContactFeignClientServiceAdapter contactAdapter;

    @BeforeEach
    public void setUp() {
        when(telegramUserService.findSecurityTokenOrGenerateAndPut(CHAT_ID)).thenReturn(TOKEN);
    }

    @Test
    public void findAll() {
        when(contactFeignClientService.findAllById(TOKEN)).thenReturn(MOCK_LIST);

        assertSame(MOCK_LIST, contactAdapter.findAll(CHAT_ID));

        verify(contactFeignClientService).findAllById(TOKEN);
    }

    @Test
    public void addOne() {
        contactAdapter.addOne(CHAT_ID, CONTACT);

        verify(contactFeignClientService).addOne(TOKEN, CONTACT);
    }

    @Test
    public void removeOne() {
        contactAdapter.removeOne(CHAT_ID, CONTACT);

        verify(contactFeignClientService).removeOne(TOKEN, CONTACT);
    }

    @Test
    public void removeMany() {
        contactAdapter.removeMany(CHAT_ID, CONTACT);

        verify(contactFeignClientService).removeMany(TOKEN, CONTACT);
    }

    @Test
    public void clear() {
        contactAdapter.clear(CHAT_ID);

        verify(contactFeignClientService).removeAllById(TOKEN);
    }
}
