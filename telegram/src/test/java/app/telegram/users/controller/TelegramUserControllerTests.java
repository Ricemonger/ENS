package app.telegram.users.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramUserControllerTests {

    private final static String TOKEN = "TOKEN";

    @Mock
    private TelegramUserControllerService telegramUserControllerService;

    @InjectMocks
    private TelegramUserController telegramUserController;

    @Test
    public void getChatId() {
        telegramUserController.getChatId(TOKEN);

        verify(telegramUserControllerService).getChatIdByToken(TOKEN);
    }
}
