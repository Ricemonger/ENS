package app.security.tg_users.controller;

import app.security.tg_users.controller.dto.UsernamePasswordRequest;
import app.security.tg_users.model.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramUserControllerServiceTests {

    private final static String TOKEN = "TOKEN";

    private static final UsernamePasswordRequest REQUEST = new UsernamePasswordRequest("username", "password");

    @Mock
    private TelegramUserService telegramUserService;

    @InjectMocks
    private TelegramUserControllerService telegramUserControllerService;

    @Test
    public void create() {
        telegramUserControllerService.create(TOKEN);

        verify(telegramUserService).create(TOKEN);
    }

    @Test
    public void delete() {
        telegramUserControllerService.delete(TOKEN);

        verify(telegramUserService).delete(TOKEN);
    }

    @Test
    public void getSecurityToken() {
        telegramUserControllerService.getSecurityToken(TOKEN);

        verify(telegramUserService).getSecurityToken(TOKEN);
    }

    @Test
    public void getAccountInfo() {
        telegramUserControllerService.getAccountInfo(TOKEN);

        verify(telegramUserService).getAccountInfo(TOKEN);
    }

    @Test
    public void link() {
        telegramUserControllerService.link(TOKEN, REQUEST);

        verify(telegramUserService).link(TOKEN, REQUEST.username(), REQUEST.password());
    }

    @Test
    public void unlinkWithDataToTelegram() {
        telegramUserControllerService.unlinkWithDataToTelegram(TOKEN);

        verify(telegramUserService).unlinkWithDataToTelegram(TOKEN);
    }

    @Test
    public void unlinkWithDataToEns() {
        telegramUserControllerService.unlinkWithDataToEns(TOKEN);

        verify(telegramUserService).unlinkWithDataToEns(TOKEN);
    }

    @Test
    public void isLinked() {
        telegramUserControllerService.isLinked(TOKEN);

        verify(telegramUserService).isLinked(TOKEN);
    }
}
