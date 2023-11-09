import app.security.abstract_users.controller.AbstractUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SecurityServerTests {

    @Autowired
    private AbstractUserController abstractUserController;
}
