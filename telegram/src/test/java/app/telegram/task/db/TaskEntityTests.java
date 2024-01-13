package app.telegram.task.db;

import app.telegram.bot.task.model.TaskType;
import app.telegram.bot.task.model.db.TaskEntity;
import app.utils.services.contact.Method;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskEntityTests {

    private final TaskEntity ENTITY = new TaskEntity("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final TaskEntity SAME_ENTITY = new TaskEntity("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final TaskEntity ALTERED_CHAT_ID = new TaskEntity("DI_TAHC", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final TaskEntity ALTERED_NAME = new TaskEntity("CHAT_ID", "EMAN", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final TaskEntity ALTERED_TASK_TIME = new TaskEntity("CHAT_ID", "EMAN", new Date(2), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final TaskEntity ALTERED_TASK_TYPE = new TaskEntity("CHAT_ID", "NAME", new Date(1), TaskType.MANY,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final TaskEntity ALTERED_CONTACT_METHOD = new TaskEntity("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.VIBER, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final TaskEntity ALTERED_CONTACT_ID = new TaskEntity("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "DI_TCATNOC", "CONTACT_NOTIFICATION");

    private final TaskEntity ALTERED_CONTACT_NOTIFICATION = new TaskEntity("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "NOITACIFITON_TCATNOC");

    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(ENTITY, SAME_ENTITY);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(ENTITY, ALTERED_CHAT_ID);
        assertNotEquals(ENTITY, ALTERED_NAME);
        assertNotEquals(ENTITY, ALTERED_TASK_TIME);
        assertNotEquals(ENTITY, ALTERED_TASK_TYPE);
        assertNotEquals(ENTITY, ALTERED_CONTACT_METHOD);
        assertNotEquals(ENTITY, ALTERED_CONTACT_ID);
        assertNotEquals(ENTITY, ALTERED_CONTACT_NOTIFICATION);
    }
}
