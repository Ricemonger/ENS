package app.telegram.bot.task.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, TaskCompositeKey> {
    void deleteAllInBatchByChatId(String chatId);

    List<TaskEntity> findAllByChatId(String chatId);
}
