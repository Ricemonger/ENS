package app.security.ens_users.controller;

import app.security.ens_users.controller.dto.EnsUserLoginRequest;
import app.security.ens_users.controller.dto.EnsUserRegisterRequest;
import app.security.ens_users.model.EnsUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EnsUserControllerServiceTests {

    private static final EnsUserRegisterRequest REGISTER_REQUEST = new EnsUserRegisterRequest("username", "password");

    private static final EnsUserLoginRequest LOGIN_REQUEST = new EnsUserLoginRequest("username", "password");

    @Mock
    private EnsUserService userService;

    @InjectMocks
    private EnsUserControllerService controllerService;

    @Test
    public void register() {
        controllerService.register(REGISTER_REQUEST);
        verify(userService).register(REGISTER_REQUEST.toUser());
    }

    @Test
    public void login() {
        controllerService.login(LOGIN_REQUEST);
        verify(userService).login(LOGIN_REQUEST.toUser());
    }
}
