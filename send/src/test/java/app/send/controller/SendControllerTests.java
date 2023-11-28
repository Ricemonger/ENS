package app.send.controller;

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

    private final static SendOneRequest REQUEST = new SendOneRequest("SMS", "1111", "text");

    @Mock
    private SendControllerService service;

    @InjectMocks
    private SendController controller;

    @Test
    public void sendOne() {
        controller.sendOne(TOKEN, REQUEST);

        verify(service).sendOne(TOKEN, REQUEST);
    }

    @Test
    public void sendAll() {
        controller.sendAll(TOKEN);

        verify(service).sendAll(TOKEN);
    }
}
