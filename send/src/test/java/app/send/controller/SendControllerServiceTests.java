package app.send.controller;

import app.send.model.SendService;
import app.utils.feign_clients.sender.dto.SendManyRequest;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SendControllerServiceTests {

    private final static String TOKEN = "TOKEN";

    private final static SendManyRequest MANY_REQUEST = new SendManyRequest("SMS", "1111", "name");

    private final static SendOneRequest ONE_REQUEST = new SendOneRequest("SMS", "1111", "text");

    @Mock
    private SendService sendService;

    @InjectMocks
    private SendControllerService sendControllerService;

    @Test
    public void sendOne() {
        sendControllerService.sendOne(TOKEN, ONE_REQUEST);

        verify(sendService).sendOne(TOKEN, ONE_REQUEST.method(), ONE_REQUEST.contactId(), ONE_REQUEST.notificationText());
    }

    @Test
    public void sendMany() {
        sendControllerService.sendMany(TOKEN, MANY_REQUEST);

        verify(sendService).sendMany(TOKEN, MANY_REQUEST.method(), MANY_REQUEST.contactId(),
                MANY_REQUEST.notificationName());
    }

    @Test
    public void sendAll() {
        sendControllerService.sendAll(TOKEN);

        verify(sendService).sendAll(TOKEN);
    }
}
