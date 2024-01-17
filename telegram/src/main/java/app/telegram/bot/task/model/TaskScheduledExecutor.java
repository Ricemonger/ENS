package app.telegram.bot.task.model;

import app.telegram.bot.feign_client_adapters.SendFeignClientServiceAdapter;
import app.telegram.bot.task.model.db.TaskRepositoryService;
import app.utils.services.sender.dto.SendManyRequest;
import app.utils.services.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskScheduledExecutor {

    private final TaskRepositoryService taskRepositoryService;

    private final SendFeignClientServiceAdapter sender;

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void executeTasks() {
        List<Task> tasks = getTasksToBeExecuted();
        removeTasksFromDatabase(tasks);
        for (Task task : tasks) {
            long chatId = Long.parseLong(task.getChatId());
            TaskType taskType = task.getTaskType();
            switch (taskType) {
                case ONE -> {
                    SendOneRequest request = new SendOneRequest(
                            task.getContactMethod().name(),
                            task.getContactId(),
                            task.getContactNotification());
                    sender.sendOne(chatId, request);
                }
                case MANY -> {
                    SendManyRequest request = new SendManyRequest(
                            task.getContactMethod().name(),
                            task.getContactId(),
                            task.getContactNotification());
                    sender.sendMany(chatId, request);
                }
                case ALL -> {
                    sender.sendAll(chatId);
                }
            }
        }
    }

    private List<Task> getTasksToBeExecuted() {
        Date currentTime = new Date();
        List<Task> result =
                taskRepositoryService.findAll().stream().filter(t -> (t.getTaskTime() != null && t.getTaskTime().before(currentTime))).toList();
        log.trace("Current Date and Time : {}, Task to be executed:{}", currentTime, result);
        return result;
    }

    private void removeTasksFromDatabase(List<Task> tasks) {
        log.trace("Tasks to be removed:{}", tasks);
        taskRepositoryService.deleteAll(tasks);
    }
}
