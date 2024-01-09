package app.telegram.task.controller;

import app.telegram.task.model.Task;
import app.telegram.task.model.db.TaskRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepositoryService repositoryService;

    public Task create(Task task) {
        return repositoryService.create(task);
    }

    public void deleteByKey(String chatId, String name) {
        repositoryService.deleteByKey(chatId, name);
    }

    public void deleteAllByChatId(String chatId) {
        repositoryService.deleteAllByChatId(chatId);
    }
}
