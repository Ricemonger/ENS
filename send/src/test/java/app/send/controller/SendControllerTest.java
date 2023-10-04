package app.send.controller;

import app.send.controller.dto.SendOneRequest;
import app.send.service.SendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import app.utils.JwtClient;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendControllerTest {

    @Mock
    private SendService sendService;

    @Mock
    private JwtClient jwtClient;

    private SendController sendController;

    @BeforeEach
    void setUp(){
        sendController = new SendController(sendService,jwtClient);
    }

    @Test
    void sendOne() {
        String token = "token";
        String method = "method";
        String id = "contactId";
        String text = "text";
        SendOneRequest request = new SendOneRequest(method,id,text);
                sendController.sendOne(token,request);
        verify(sendService).sendOne(token,null,method,id,text);
    }

    @Test
    void sendAll() {
        String token = "token";
        sendController.sendAll(token);
        verify(sendService).sendAll(token,null);
        verifyNoMoreInteractions(sendService);
    }
}