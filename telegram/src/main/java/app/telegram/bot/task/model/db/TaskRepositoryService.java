package app.telegram.bot.task.model.db;

import app.telegram.bot.exceptions.user.TaskAlreadyExistsException;
import app.telegram.bot.task.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskRepositoryService {

    private final TaskRepository taskRepository;

    public Task create(Task task) {
        TaskEntity entity = toEntity(task);
        if (taskRepository.existsById(new TaskCompositeKey(entity.getChatId(), entity.getName()))) {
            log.info("Task-{} already exists, exception thrown in create() method", task);
            throw new TaskAlreadyExistsException();
        } else {
            TaskEntity saved = taskRepository.save(entity);
            return toTask(saved);
        }
    }

    public List<Task> findAllByChatId(String chatId) {
        return taskRepository.findAllByChatId(chatId).stream().map(this::toTask).toList();
    }

    public void deleteByKey(String chatId, String name) {
        TaskCompositeKey key = new TaskCompositeKey(chatId, name);
        taskRepository.deleteById(key);
    }

    public void deleteAllByChatId(String chatId) {
        taskRepository.deleteAllByChatId(chatId);
    }

    private TaskEntity toEntity(Task task) {
        return new TaskEntity(task.getChatId(), task.getName(),
                task.getTaskTime(), task.getTaskType(),
                task.getContactMethod(), task.getContactId(), task.getContactNotification());
    }

    private Task toTask(TaskEntity entity) {
        return new Task(entity);
    }
}
