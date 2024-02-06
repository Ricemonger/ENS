package app.security.any_user.model.db;

import app.security.any_users.AnyUser;
import app.security.any_users.model.db.AnyUserEntity;
import app.security.any_users.model.db.AnyUserRepository;
import app.security.any_users.model.db.AnyUserRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AnyUserRepositoryServiceTests {

    private final static AnyUserEntity ENTITY = new AnyUserEntity();

    @Autowired
    private AnyUserRepository repository;

    @Autowired
    private AnyUserRepositoryService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void existsByIdShouldReturnTrueIfExistsRightUserAndFalseIfNot() {
        String accountId = repository.save(ENTITY).getAccountId();

        assertTrue(service.existsById(accountId));
        assertFalse(service.existsById(accountId.substring(1)));
    }

    @Test
    public void deleteByIdShouldDeleteRightUser() {
        String accountId = repository.save(new AnyUserEntity()).getAccountId();
        String anotherAccountId = repository.save(new AnyUserEntity()).getAccountId();

        service.deleteById(accountId);

        List<AnyUserEntity> expected = Collections.singletonList(new AnyUserEntity(anotherAccountId));
        List<AnyUserEntity> trueResult = repository.findAll();

        assertEquals(expected, trueResult);
    }

    @Test
    public void findByIdOrThrowShouldFindRightObject() {
        String accountId = repository.save(ENTITY).getAccountId();
        repository.save(ENTITY);

        AnyUser inDb = service.findByIdOrThrow(accountId);

        assertEquals(new AnyUser(accountId), inDb);
    }

    @Test
    public void findByIdOrThrowShouldThrowIfDoesntExist() {
        Executable executable = () -> service.findByIdOrThrow("1313131313545");

        assertThrows(NoSuchElementException.class, executable);
    }

    @Test
    public void saveShouldCreate() {
        String accountId = service.save(new AnyUser()).getAccountId();

        AnyUserEntity inDb = repository.findAll().get(0);

        assertEquals(new AnyUserEntity(accountId), inDb);
    }

    @Test
    public void saveShouldUpdate() {
        Executable executable = () -> {
            AnyUser saved = service.save(new AnyUser());
            service.save(saved);

            AnyUserEntity inDb = repository.findAll().get(0);
            assertEquals(new AnyUserEntity(saved.getAccountId()), inDb);
        };

        assertDoesNotThrow(executable);
    }

    @Test
    public void findAllShouldGetAllEntriesFromDb() {
        repository.save(new AnyUserEntity());
        repository.save(new AnyUserEntity());
        repository.save(new AnyUserEntity());
        repository.save(new AnyUserEntity());
        repository.save(new AnyUserEntity());

        List<AnyUser> inDb = repository.findAll().stream().map(AnyUser::new).toList();
        List<AnyUser> found = service.findAll();

        assertTrue(inDb.containsAll(found) && found.containsAll(inDb));
    }

    @Test
    public void deleteAllShouldRemoveAllFromDb() {
        repository.save(new AnyUserEntity());
        repository.save(new AnyUserEntity());
        repository.save(new AnyUserEntity());
        repository.save(new AnyUserEntity());

        service.deleteAll();

        assertEquals(0, repository.findAll().size());
    }
}
