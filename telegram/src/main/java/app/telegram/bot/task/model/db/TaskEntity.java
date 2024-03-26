package app.telegram.bot.task.model.db;

import app.telegram.bot.task.model.TaskType;
import app.utils.services.contact.Method;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "task")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TaskCompositeKey.class)
public class TaskEntity {
    @Id
    private String chatId;

    @Id
    private String name;

    @Column(columnDefinition = "TIMESTAMP")
    private Date taskTime;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    private Method contactMethod;

    private String contactId;

    private String contactNotification;

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof TaskEntity entity) {

            boolean chatIdEq = fieldsEqual(this.chatId, entity.chatId);

            boolean nameEq = fieldsEqual(this.name, entity.name);

            boolean taskTimeEq;
            if (this.taskTime == null) {
                taskTimeEq = entity.taskTime == null;
            } else if (entity.taskTime == null) {
                taskTimeEq = false;
            } else {
                long thisTime = this.taskTime.getTime();
                long entityTime = entity.taskTime.getTime();
                taskTimeEq = thisTime == entityTime;
            }

            boolean taskTypeEq = fieldsEqual(this.taskType, entity.taskType);

            boolean contactMethodEq = fieldsEqual(this.contactMethod, entity.contactMethod);

            boolean contactIdEq = fieldsEqual(this.contactId, entity.contactId);

            boolean contactNotificationEq = fieldsEqual(this.contactNotification, entity.contactNotification);

            return chatIdEq && nameEq && taskTimeEq && taskTypeEq && contactMethodEq && contactIdEq && contactNotificationEq;
        } else {
            return false;
        }
    }

    private <T> boolean fieldsEqual(T obj1, T obj2) {
        if (obj1 == null) {
            return obj2 == null;
        } else {
            return obj1.equals(obj2);
        }
    }
}
