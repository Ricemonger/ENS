package app.telegram.task.model.db;

import app.telegram.task.model.TaskType;
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
}
