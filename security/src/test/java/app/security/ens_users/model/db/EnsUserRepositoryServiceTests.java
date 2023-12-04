package app.security.ens_users.model.db;

import app.security.ens_users.EnsUser;
import app.utils.services.security.exceptions.UserDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EnsUserRepositoryServiceTests {

    private final static EnsUserEntity ENTITY = new EnsUserEntity("username", "password");

    private final static EnsUserEntity ALTERED_USERNAME = new EnsUserEntity("emanresu", "password");

    private final static EnsUserEntity ALTERED_PASSWORD = new EnsUserEntity("username", "drowssap");

    private final static EnsUser USER = new EnsUser("username", "password");


    @Autowired
    private EnsUserRepository repository;

    @Autowired
    private EnsUserRepositoryService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void saveShouldCreateUserInDbAndReturnIt() {
        EnsUser user = service.save(USER);

        EnsUserEntity inDB = repository.findAll().get(0);
        EnsUser userInDb = new EnsUser(inDB.getAccountId(), inDB.getUsername(), inDB.getPassword());
        assertEquals(user, userInDb);
    }

    @Test
    public void saveShouldUpdateUserInDbAndReturnIt() {
        EnsUserEntity saved = repository.save(ENTITY);

        EnsUser updated = new EnsUser(saved.getAccountId(), ALTERED_PASSWORD.getUsername(), ALTERED_PASSWORD.getPassword());
        service.save(updated);

        EnsUserEntity updatedEntity = new EnsUserEntity(updated.getAccountId(), updated.getUsername(),
                updated.getPassword());

        assertEquals(updatedEntity, repository.findAll().get(0));
    }

    @Test
    public void existsByAccountIdShouldReturnTrueIfExists() {
        String accountId = repository.save(ENTITY).getAccountId();

        assertTrue(service.existsByAccountId(accountId));
    }

    @Test
    public void existsByAccountIdShouldReturnFalseIfDoesntExist() {
        repository.save(ENTITY).getAccountId();

        assertFalse(service.existsByAccountId("hjfhjghjghjghjg"));
    }

    @Test
    public void findByIdOrThrowShouldGetIfExists() {
        EnsUserEntity savedEntity = repository.save(ENTITY);
        EnsUser savedUser = new EnsUser(savedEntity.getAccountId(), ENTITY.getUsername(),
                ENTITY.getPassword());


        EnsUser inDb = service.findByIdOrThrow(ENTITY.getUsername());

        assertEquals(savedUser, inDb);
    }

    @Test
    public void findByIdOrThrowShouldThrowIfDoesntExist() {
        repository.save(ENTITY);

        Executable executable = () -> service.findByIdOrThrow(ALTERED_USERNAME.getUsername());

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void findByAccountIdOrThrowShouldGetIfExists() {
        String accountId = repository.save(ENTITY).getAccountId();

        EnsUser expected = new EnsUser(accountId, ENTITY.getUsername(), ENTITY.getPassword());

        assertEquals(expected, service.findByAccountIdOrThrow(accountId));
    }

    @Test
    public void findByAccountIdShouldThrowIfDoesntExist() {
        repository.save(ENTITY);

        Executable executable = () -> service.findByAccountIdOrThrow("ytrewq234567");

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void deleteAllShouldRemoveAllEntriesFromDb() {
        repository.save(ENTITY);
        repository.save(ALTERED_USERNAME);

        service.deleteAll();

        assertEquals(0, repository.findAll().size());
    }
}
