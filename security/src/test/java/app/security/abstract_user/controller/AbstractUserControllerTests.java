package app.security.abstract_user.controller;

import app.security.abstract_users.controller.AbstractUserController;
import app.security.abstract_users.controller.AbstractUserControllerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AbstractUserControllerTests {

    private final static String TOKEN = "token";

    @Mock
    private AbstractUserControllerService service;

    @InjectMocks
    private AbstractUserController controller;

    @Test
    public void getAccountId() {
        controller.getAccountId(TOKEN);
        verify(service).getAccountId(TOKEN);
    }
}
