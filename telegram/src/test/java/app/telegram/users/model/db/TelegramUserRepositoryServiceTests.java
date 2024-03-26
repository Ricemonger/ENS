package app.telegram.users.model.db;

import app.telegram.users.model.TelegramUser;
import app.utils.services.telegram.exceptions.TelegramUserDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TelegramUserRepositoryServiceTests {

    private final static String ID = "CHAT_ID";

    private final static String AID = "ANOTHER_CHAT_ID";

    private final static String TT = "TELEGRAM_TOKEN";

    private final static Date TD = new Date(1_000);

    private final static String ST = "SECURITY_TOKEN";

    private final static Date SD = new Date(2_000);

    private final static TelegramUser USER = new TelegramUser(ID, TT, TD, ST, SD);

    private final static TelegramUserEntity ENTITY = new TelegramUserEntity(ID, TT, TD, ST, SD);

    private final static TelegramUserEntity ANOTHER_ENTITY = new TelegramUserEntity(AID, TT, TD, ST, SD);

    @SpyBean
    private TelegramUserRepository repository;

    @Autowired
    private TelegramUserRepositoryService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void save() {
        TelegramUser user = service.save(USER);

        verify(repository).save(ENTITY);

        assertEquals(USER, user);
    }

    @Test
    public void delete() {
        service.delete(USER);

        verify(repository).delete(ENTITY);
    }

    @Test
    public void findByChatIdOrThrowShouldFind() {
        repository.save(ENTITY);

        TelegramUser user = service.findByChatIdOrThrow(ID);

        verify(repository).findById(ID);

        assertEquals(USER, user);
    }

    @Test
    public void findByChatIdOrThrowShouldThrowIfNotFound() {
        repository.save(ANOTHER_ENTITY);

        Executable executable = () -> service.findByChatIdOrThrow(ID);

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    @Test
    public void deleteAll() {
        reset(repository);

        service.deleteAll();

        verify(repository).deleteAll();
    }

    @Test
    public void findAll() {
        repository.save(ENTITY);
        repository.save(ANOTHER_ENTITY);

        List<TelegramUser> expected = new ArrayList<>();
        expected.add(USER);
        expected.add(new TelegramUser(ANOTHER_ENTITY));

        List<TelegramUser> result = service.findAll();

        verify(repository).findAll();

        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }
}
