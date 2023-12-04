package app.security.any_user.model;

import app.security.any_users.AnyUser;
import app.security.any_users.model.AnyUserService;
import app.security.any_users.model.db.AnyUserRepositoryService;
import app.utils.feign_clients.security_abstract.exceptions.UserDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AnyUserServiceTests {

    @Autowired
    private AnyUserRepositoryService repositoryService;

    @Autowired
    private AnyUserService service;

    @BeforeEach
    public void setUp() {
        repositoryService.deleteAll();
    }

    @Test
    public void createShouldSaveUserInDb() {
        List<AnyUser> saved = new ArrayList<>();
        saved.add(service.create());
        saved.add(service.create());
        saved.add(service.create());

        List<AnyUser> inDb = repositoryService.findAll();
        assertTrue(saved.containsAll(inDb) && inDb.containsAll(saved));
    }

    @Test
    public void deleteShouldRemoveUserFromDbAndReturnItsValue() {
        AnyUser toRemove = repositoryService.save(new AnyUser());
        List<AnyUser> expectedLeftover = new ArrayList<>();
        expectedLeftover.add(repositoryService.save(new AnyUser()));
        expectedLeftover.add(repositoryService.save(new AnyUser()));
        expectedLeftover.add(repositoryService.save(new AnyUser()));
        expectedLeftover.add(repositoryService.save(new AnyUser()));

        AnyUser removed = service.delete(toRemove.getAccountId());

        List<AnyUser> inDb = repositoryService.findAll();

        assertEquals(toRemove, removed);
        assertTrue(expectedLeftover.containsAll(inDb) && inDb.containsAll(expectedLeftover));
    }

    @Test
    public void deleteShouldThrowIfUserDoesntExist() {
        repositoryService.save(new AnyUser());
        repositoryService.save(new AnyUser());
        repositoryService.save(new AnyUser());
        repositoryService.save(new AnyUser());

        Executable executable = () -> service.delete("132445466");

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void doesUserExistShouldReturnTrueIfRight() {
        AnyUser created = repositoryService.save(new AnyUser());

        assertTrue(service.doesUserExist(created.getAccountId()));
    }

    @Test
    public void doesUserExistShouldReturnFalseINotRight() {
        assertFalse(service.doesUserExist("6767676"));
    }
}
