package app.telegram.task.model.db;

import app.telegram.task.model.Task;
import app.telegram.task.model.TaskAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
