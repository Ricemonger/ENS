package app.telegram.task;

import app.telegram.task.model.Task;
import app.telegram.task.model.TaskType;
import app.utils.services.contact.Method;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTests {
    private final Task TASK = new Task("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final Task SAME_TASK = new Task("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final Task ALTERED_CHAT_ID = new Task("DI_TAHC", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final Task ALTERED_NAME = new Task("CHAT_ID", "EMAN", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final Task ALTERED_TASK_TIME = new Task("CHAT_ID", "EMAN", new Date(2), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final Task ALTERED_TASK_TYPE = new Task("CHAT_ID", "NAME", new Date(1), TaskType.MANY,
            Method.SMS, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final Task ALTERED_CONTACT_METHOD = new Task("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.VIBER, "CONTACT_ID", "CONTACT_NOTIFICATION");

    private final Task ALTERED_CONTACT_ID = new Task("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "DI_TCATNOC", "CONTACT_NOTIFICATION");

    private final Task ALTERED_CONTACT_NOTIFICATION = new Task("CHAT_ID", "NAME", new Date(1), TaskType.ONE,
            Method.SMS, "CONTACT_ID", "NOITACIFITON_TCATNOC");

    @Test
    public void equalsShouldReturnTrueIfSame() {
        assertEquals(TASK, SAME_TASK);
    }

    @Test
    public void equalsShouldReturnFalseIfNotSame() {
        assertNotEquals(TASK, ALTERED_CHAT_ID);
        assertNotEquals(TASK, ALTERED_NAME);
        assertNotEquals(TASK, ALTERED_TASK_TIME);
        assertNotEquals(TASK, ALTERED_TASK_TYPE);
        assertNotEquals(TASK, ALTERED_CONTACT_METHOD);
        assertNotEquals(TASK, ALTERED_CONTACT_ID);
        assertNotEquals(TASK, ALTERED_CONTACT_NOTIFICATION);
    }
}
