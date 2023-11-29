package app.send.controller;

import app.utils.feign_clients.sender.dto.SendManyRequest;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SendControllerTests {

    private final static String TOKEN = "TOKEN";

    private final static SendManyRequest MANY_REQUEST = new SendManyRequest("SMS", "1111", "name");

    private final static SendOneRequest ONE_REQUEST = new SendOneRequest("SMS", "1111", "text");

    @Mock
    private SendControllerService service;

    @InjectMocks
    private SendController controller;

    @Test
    public void sendOne() {
        controller.sendOne(TOKEN, ONE_REQUEST);

        verify(service).sendOne(TOKEN, ONE_REQUEST);
    }

    @Test
    public void sendMany() {
        controller.sendMany(TOKEN, MANY_REQUEST);

        verify(service).sendMany(TOKEN, MANY_REQUEST);
    }

    @Test
    public void sendAll() {
        controller.sendAll(TOKEN);

        verify(service).sendAll(TOKEN);
    }
}
