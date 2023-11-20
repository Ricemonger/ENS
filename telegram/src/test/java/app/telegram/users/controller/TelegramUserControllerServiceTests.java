package app.telegram.users.controller;

import app.telegram.users.model.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramUserControllerServiceTests {

    private final static String TOKEN = "TOKEN";

    @Mock
    private TelegramUserService telegramUserService;

    @InjectMocks
    private TelegramUserControllerService telegramUserControllerService;

    @Test
    public void getChatIdByToken() {
        telegramUserControllerService.getChatIdByToken(TOKEN);

        verify(telegramUserService).getChatIdByToken(TOKEN);
    }
}
