package app.security.tg_users.controller;

import app.security.tg_users.controller.dto.UsernamePasswordRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramUserControllerTests {

    private static final String TOKEN = "TOKEN";

    private static final UsernamePasswordRequest REQUEST = new UsernamePasswordRequest("username", "password");

    @Mock
    private TelegramUserControllerService service;

    @InjectMocks
    private TelegramUserController controller;

    @Test
    public void create() {
        controller.create(TOKEN);

        verify(service).create(TOKEN);
    }

    @Test
    public void delete() {
        controller.delete(TOKEN);

        verify(service).delete(TOKEN);
    }

    @Test
    public void getSecurityToken() {
        controller.getSecurityToken(TOKEN);

        verify(service).getSecurityToken(TOKEN);
    }

    @Test
    public void getAccountInfo() {
        controller.getAccountInfo(TOKEN);

        verify(service).getAccountInfo(TOKEN);
    }

    @Test
    public void link() {
        controller.link(TOKEN, REQUEST);

        verify(service).link(TOKEN, REQUEST);
    }

    @Test
    public void unlink() {
        controller.unlink(TOKEN);

        verify(service).unlinkWithDataToTelegram(TOKEN);
    }

    @Test
    public void isLinked() {
        controller.isLinked(TOKEN);

        verify(service).isLinked(TOKEN);
    }
}
