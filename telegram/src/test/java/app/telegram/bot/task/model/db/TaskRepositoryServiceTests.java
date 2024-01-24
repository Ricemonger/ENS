package app.telegram.bot.task.model.db;

import app.telegram.bot.exceptions.user.TaskAlreadyExistsException;
import app.telegram.bot.task.model.Task;
import app.telegram.bot.task.model.TaskType;
import app.utils.services.contact.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskRepositoryServiceTests {

    private final static Date date = new Date(0);

    private final static TaskEntity TASK_ENTITY = new TaskEntity(
            "chatId", "name",
            date, TaskType.ONE,
            Method.SMS, "phoneNumber", "notificationText");

    private final static Task TASK = new Task(TASK_ENTITY);

    private final static TaskEntity SAME_USER_ANOTHER_TASK_ENTITY = new TaskEntity(
            "chatId", "eman",
            date, TaskType.ONE,
            Method.SMS, "phoneNumber", "notificationText");

    private final static Task SAME_USER_ANOTHER_TASK = new Task(SAME_USER_ANOTHER_TASK_ENTITY);

    private final static TaskEntity ANOTHER_USER_SAME_TASK_ENTITY = new TaskEntity(
            "another chatId", "name",
            date, TaskType.ONE,
            Method.SMS, "phoneNumber", "notificationText");

    private final static Task ANOTHER_USER_SAME_TASK = new Task(ANOTHER_USER_SAME_TASK_ENTITY);

    @SpyBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskRepositoryService taskRepositoryService;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAllInBatch();
    }

    @Test
    public void createShouldSaveIfDoesntExist() {
        taskRepositoryService.create(TASK);
        taskRepositoryService.create(ANOTHER_USER_SAME_TASK);

        List<TaskEntity> expected = new ArrayList<>();
        expected.add(TASK_ENTITY);
        expected.add(ANOTHER_USER_SAME_TASK_ENTITY);

        assertEquals(2, taskRepository.count());

        assertTrue(taskRepository.findAll().containsAll(expected));
    }

    @Test
    public void createShouldThrowIfAlreadyExists() {
        taskRepository.save(TASK_ENTITY);

        Executable executable = () -> {
            taskRepositoryService.create(TASK);
        };

        assertThrows(TaskAlreadyExistsException.class, executable);
    }

    @Test
    public void findAllByChatIdShouldReturnAllOnlyByChatId() {
        taskRepository.save(ANOTHER_USER_SAME_TASK_ENTITY);

        taskRepository.save(TASK_ENTITY);
        taskRepository.save(SAME_USER_ANOTHER_TASK_ENTITY);

        List<Task> expected = new ArrayList<>();
        expected.add(TASK);
        expected.add(SAME_USER_ANOTHER_TASK);

        List<Task> result = taskRepositoryService.findAllByChatId(TASK_ENTITY.getChatId());

        assertEquals(2, result.size());

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    public void deleteByKeyShouldRemoveOnlyByKey() {
        taskRepository.save(ANOTHER_USER_SAME_TASK_ENTITY);

        taskRepository.save(TASK_ENTITY);
        taskRepository.save(SAME_USER_ANOTHER_TASK_ENTITY);

        taskRepositoryService.deleteByKey(TASK_ENTITY.getChatId(), TASK_ENTITY.getName());

        List<Task> expected = new ArrayList<>();
        expected.add(ANOTHER_USER_SAME_TASK);
        expected.add(SAME_USER_ANOTHER_TASK);

        List<Task> result = taskRepository.findAll().stream().map(Task::new).toList();

        assertEquals(2, result.size());

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    public void deleteAllByChatIdShouldRemoveAllOnlyByChatId() {
        taskRepository.save(ANOTHER_USER_SAME_TASK_ENTITY);

        taskRepository.save(TASK_ENTITY);
        taskRepository.save(SAME_USER_ANOTHER_TASK_ENTITY);

        taskRepositoryService.deleteAllByChatId(TASK_ENTITY.getChatId());

        List<Task> result = taskRepository.findAll().stream().map(Task::new).toList();

        assertEquals(1, result.size());

        assertEquals(Collections.singletonList(ANOTHER_USER_SAME_TASK), result);
    }

    @Test
    public void findAllShouldReturnAll() {
        taskRepository.save(ANOTHER_USER_SAME_TASK_ENTITY);

        taskRepository.save(TASK_ENTITY);
        taskRepository.save(SAME_USER_ANOTHER_TASK_ENTITY);

        List<Task> expected = new ArrayList<>();
        expected.add(TASK);
        expected.add(SAME_USER_ANOTHER_TASK);
        expected.add(ANOTHER_USER_SAME_TASK);

        List<Task> result = taskRepositoryService.findAll();

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    public void deleteAllShouldRemoveAllFromList() {
        taskRepository.save(ANOTHER_USER_SAME_TASK_ENTITY);

        taskRepository.save(TASK_ENTITY);
        taskRepository.save(SAME_USER_ANOTHER_TASK_ENTITY);

        List<Task> toDelete = new ArrayList<>();
        toDelete.add(TASK);
        toDelete.add(ANOTHER_USER_SAME_TASK);

        taskRepositoryService.deleteAll(toDelete);

        assertEquals(SAME_USER_ANOTHER_TASK_ENTITY, taskRepository.findAll().get(0));

        assertEquals(1, taskRepository.findAll().size());
    }
}
