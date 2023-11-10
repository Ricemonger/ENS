package app.security.abstract_user.controller;

import app.security.abstract_users.controller.AbstractUserControllerService;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AbstractUserControllerServiceTests {

    private final static String TOKEN = "token";

    @Mock
    private AbstractUserJwtUtil abstractUserJwtUtil;

    @InjectMocks
    private AbstractUserControllerService service;

    @Test
    public void getAccountId() {
        service.getAccountId(TOKEN);
        verify(abstractUserJwtUtil).extractAccountId(TOKEN);
    }
}
