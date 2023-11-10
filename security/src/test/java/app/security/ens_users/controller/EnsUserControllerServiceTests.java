package app.security.ens_users.controller;

import app.security.ens_users.controller.dto.EnsUserLoginRequest;
import app.security.ens_users.controller.dto.EnsUserRegisterRequest;
import app.security.ens_users.model.db.EnsUserRepositoryService;
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
    private EnsUserRepositoryService repositoryService;

    @InjectMocks
    private EnsUserControllerService controllerService;

    @Test
    public void register() {
        controllerService.register(REGISTER_REQUEST);
        verify(repositoryService).register(REGISTER_REQUEST.toUser());
    }

    @Test
    public void login() {
        controllerService.login(LOGIN_REQUEST);
        verify(repositoryService).login(LOGIN_REQUEST.toUser());
    }
}
