package app.telegram.bot.task.controller;

import app.telegram.bot.task.model.Task;
import app.telegram.bot.task.model.db.TaskRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepositoryService repositoryService;

    public Task create(Task task) {
        return repositoryService.create(task);
    }

    public void deleteByKey(Long chatId, String name) {
        repositoryService.deleteByKey(String.valueOf(chatId), name);
    }

    public void deleteAllByChatId(Long chatId) {
        repositoryService.deleteAllByChatId(String.valueOf(chatId));
    }

    public List<Task> findAllByChatId(Long chatId) {
        return repositoryService.findAllByChatId(String.valueOf(chatId));
    }
}
