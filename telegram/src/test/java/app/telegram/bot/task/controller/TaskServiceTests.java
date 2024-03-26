package app.telegram.bot.task.controller;

import app.telegram.bot.task.model.Task;
import app.telegram.bot.task.model.TaskType;
import app.telegram.bot.task.model.db.TaskRepositoryService;
import app.utils.services.contact.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    private final static Date date = new Date(0);

    private final static Task TASK = new Task(
            "32323", "name",
            date, TaskType.ONE,
            Method.SMS, "phoneNumber", "notificationText");

    @Mock
    private TaskRepositoryService taskRepositoryService;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void create() {
        taskService.create(TASK);

        verify(taskRepositoryService).create(TASK);
    }

    @Test
    public void deleteByKey() {
        taskService.deleteByKey(Long.parseLong(TASK.getChatId()), TASK.getName());

        verify(taskRepositoryService).deleteByKey(TASK.getChatId(), TASK.getName());
    }

    @Test
    public void deleteAllByChatId() {
        taskService.deleteAllByChatId(Long.parseLong(TASK.getChatId()));

        verify(taskRepositoryService).deleteAllByChatId(TASK.getChatId());
    }

    @Test
    public void findAllByChatId() {
        taskService.findAllByChatId(Long.parseLong(TASK.getChatId()));

        verify(taskRepositoryService).findAllByChatId(TASK.getChatId());
    }
}
