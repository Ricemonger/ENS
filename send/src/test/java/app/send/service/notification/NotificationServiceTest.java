package app.send.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationClient notificationClient;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationClient);
    }

    @Test
    void findOneByPrimaryKey() {
        String token = "token";
        String notificationName = "notification";
        NotificationNameRequest request = new NotificationNameRequest(notificationName);
        try {
            notificationService.findOneByPrimaryKey(token, notificationName);
        } catch (NoSuchElementException e) {
        }
        verify(notificationClient).findAllByPrimaryKey(token, request);
    }

    @Test
    void getMap() {
        String token = "token";
        notificationService.getMapByAccountId(token);
        verify(notificationClient).findAllByAccountId(token);
    }
}