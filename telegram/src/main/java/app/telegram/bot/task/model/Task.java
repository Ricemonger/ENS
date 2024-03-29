package app.telegram.bot.task.model;

import app.telegram.bot.task.model.db.TaskEntity;
import app.utils.services.contact.Method;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private TaskEntity entity = new TaskEntity();

    public Task(String chatId, String name, Date taskTime, TaskType taskType, Method contactMethod, String contactId, String contactNotification) {
        this(chatId, name, taskTime, taskType, contactMethod, contactId);
        entity.setContactNotification(contactNotification);
    }

    public Task(String chatId, String name, Date taskTime, TaskType taskType, Method contactMethod, String contactId) {
        this(chatId, name, taskTime, taskType);
        entity.setContactMethod(contactMethod);
        entity.setContactId(contactId);
    }

    public Task(String chatId, String name, Date taskTime, TaskType taskType) {
        this(chatId, name);
        entity.setTaskTime(taskTime);
        entity.setTaskType(taskType);
    }

    public Task(String chatId, String name) {
        entity.setChatId(chatId);
        entity.setName(name);
    }

    public String getChatId() {
        return entity.getChatId();
    }

    public void setChatId(String chatId) {
        entity.setChatId(chatId);
    }

    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
        entity.setName(name);
    }

    public Date getTaskTime() {
        return entity.getTaskTime();
    }

    public void setTaskTime(Date taskTime) {
        entity.setTaskTime(taskTime);
    }

    public TaskType getTaskType() {
        return entity.getTaskType();
    }

    public void setTaskType(TaskType taskType) {
        entity.setTaskType(taskType);
    }

    public Method getContactMethod() {
        return entity.getContactMethod();
    }

    public void setContactMethod(Method contactMethod) {
        entity.setContactMethod(contactMethod);
    }

    public String getContactId() {
        return entity.getContactId();
    }

    public void setContactId(String contactId) {
        entity.setContactId(contactId);
    }

    public String getContactNotification() {
        return entity.getContactNotification();
    }

    public void setContactNotification(String contactNotification) {
        entity.setContactNotification(contactNotification);
    }

    public String toString() {
        if (getTaskType() == null) {
            String sb = "chatId=" + getChatId() +
                    ", name=" + getName();
            return sb;
        } else {
            return String.format("Task(chatId=%s, name=%s, taskTime=%s, taskType=%s, contactMethod=%s, contactId=%s, " +
                            "contactNotification=%s)",
                    getChatId(), getName(),
                    getTaskTime(), getTaskType(),
                    getContactMethod(), getContactId(), getContactNotification());
        }
    }
}
