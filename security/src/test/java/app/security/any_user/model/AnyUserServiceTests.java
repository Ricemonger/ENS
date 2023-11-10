package app.security.any_user.model;

import app.security.any_users.model.AnyUserService;
import app.security.any_users.model.db.AnyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AnyUserServiceTests {

    @Autowired
    private AnyUserRepository repository;

    @Autowired
    private AnyUserService repositoryService;

    @Autowired
    private AnyUserService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

}
