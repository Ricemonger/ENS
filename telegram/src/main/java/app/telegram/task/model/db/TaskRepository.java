package app.telegram.task.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, TaskCompositeKey> {
    void deleteAllByChatId(String chatId);
}
