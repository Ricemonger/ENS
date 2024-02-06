package app.security.tg_users.model.db;

import app.security.tg_users.TelegramUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class TelegramUserRepositoryServiceTests {

    private final static String CHAT_ID = "1111";

    private final static String ALTERED_CHAT_ID = "8888";

    @SpyBean
    private TelegramUserRepository repository;

    @Autowired
    private TelegramUserRepositoryService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAllInBatch();
    }

    @Test
    public void saveShouldCallSaveOnRepository() {
        service.save(new TelegramUser(CHAT_ID));

        TelegramUserEntity entity = repository.findAll().get(0);

        assertEquals(CHAT_ID, entity.getChatId());

        verify(repository).save(entity);
    }

    @Test
    public void deleteShouldCallDeleteOnRepository() {
        TelegramUserEntity added = repository.save(new TelegramUserEntity(CHAT_ID));

        service.delete(CHAT_ID);

        assertEquals(0, repository.findAll().size());

        verify(repository).getReferenceById(CHAT_ID);

        assertNull(added.getAnyUserEntity().getTelegramUserEntity());

        verify(repository).delete(added);
    }

    @Test
    public void findByChatIdOrThrowShouldGetRightEntry() {
        repository.save(new TelegramUserEntity(ALTERED_CHAT_ID));
        String accountIdRepository = repository.save(new TelegramUserEntity(CHAT_ID)).getAccountId();

        TelegramUser user = service.findByChatIdOrThrow(CHAT_ID);

        assertEquals(accountIdRepository, user.getAccountId());
        assertEquals(CHAT_ID, user.getChatId());

        verify(repository).findById(CHAT_ID);
    }

    @Test
    public void findByChatIdShouldThrowIfDoesntExist() {
        repository.save(new TelegramUserEntity(ALTERED_CHAT_ID));

        Executable executable = () -> service.findByChatIdOrThrow(CHAT_ID);

        assertThrows(NoSuchElementException.class, executable);

        verify(repository).findById(CHAT_ID);
    }

    @Test
    public void findByAccountIdShouldGetRightEntry() {
        repository.save(new TelegramUserEntity(ALTERED_CHAT_ID));
        String accountIdRepository = repository.save(new TelegramUserEntity(CHAT_ID)).getAccountId();

        TelegramUser found = service.findByAccountIdOrThrow(accountIdRepository);

        assertEquals(CHAT_ID, found.getChatId());
        assertEquals(accountIdRepository, found.getAccountId());

        verify(repository).findByAnyUserEntityAccountId(accountIdRepository);
    }

    @Test
    public void findByAccountIdShouldThrowIfDoesntExist() {
        repository.save(new TelegramUserEntity(ALTERED_CHAT_ID));

        Executable executable = () -> service.findByAccountIdOrThrow("ACCOUNT_ID");

        assertThrows(NoSuchElementException.class, executable);

        verify(repository).findByAnyUserEntityAccountId("ACCOUNT_ID");
    }

    @Test
    public void existsByChatIdShouldReturnTrueIfExists() {
        repository.save(new TelegramUserEntity(CHAT_ID));

        assertTrue(service.existsByChatId(CHAT_ID));

        verify(repository).existsById(CHAT_ID);
    }

    @Test
    public void existsByChatIdShouldReturnFalseIfDoesntExist() {
        repository.save(new TelegramUserEntity(ALTERED_CHAT_ID));

        assertFalse(service.existsByChatId(CHAT_ID));

        verify(repository).existsById(CHAT_ID);
    }

    @Test
    public void existsByAccountIdShouldReturnTrueIfExists() {
        String accountId = repository.save(new TelegramUserEntity(CHAT_ID)).getAccountId();

        assertTrue(service.existsByAccountId(accountId));

        verify(repository).existsByAnyUserEntityAccountId(accountId);
    }

    @Test
    public void existsByAccountIdShouldReturnFalseIfDoesntExist() {
        repository.save(new TelegramUserEntity(CHAT_ID));

        assertFalse(service.existsByAccountId("-098765432"));

        verify(repository).existsByAnyUserEntityAccountId("-098765432");
    }

    @Test
    public void findAllShouldReturnAllEntries() {
        repository.save(new TelegramUserEntity(CHAT_ID));
        repository.save(new TelegramUserEntity(ALTERED_CHAT_ID));

        assertEquals(2, service.findAll().size());

        verify(repository).findAll();
    }

    @Test
    public void deleteAllShouldCallDeleteAllInBatch() {
        repository.save(new TelegramUserEntity(CHAT_ID));
        repository.save(new TelegramUserEntity(ALTERED_CHAT_ID));

        reset(repository);
        service.deleteAll();

        assertEquals(0, repository.findAll().size());

        verify(repository).deleteAllInBatch();
    }
}
