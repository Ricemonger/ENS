package app.security.ens_users.controller;

import app.security.ens_users.controller.dto.EnsUserLoginRequest;
import app.security.ens_users.controller.dto.EnsUserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EnsUserControllerTests {

    private static final EnsUserRegisterRequest REGISTER_REQUEST = new EnsUserRegisterRequest("username", "password");

    private static final EnsUserLoginRequest LOGIN_REQUEST = new EnsUserLoginRequest("username", "password");

    @Mock
    private EnsUserControllerService service;

    @InjectMocks
    private EnsUserController controller;

    @Test
    public void register() {
        controller.register(REGISTER_REQUEST);
        verify(service).register(REGISTER_REQUEST);
    }

    @Test
    public void login() {
        controller.login(LOGIN_REQUEST);
        verify(service).login(LOGIN_REQUEST);
    }
}
