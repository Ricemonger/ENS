package app.security.any_user.model.db;

import app.security.any_users.model.db.AnyUserRepository;
import app.security.any_users.model.db.AnyUserRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AnyUserRepositoryServiceTests {

    @Autowired
    private AnyUserRepository repository;

    @Autowired
    private AnyUserRepositoryService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }


}
