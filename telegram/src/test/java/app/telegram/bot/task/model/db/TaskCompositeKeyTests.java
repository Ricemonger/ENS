package app.telegram.bot.task.model.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskCompositeKeyTests {

    private final static TaskCompositeKey KEY = new TaskCompositeKey("chatId", "name");

    private final static TaskCompositeKey SAME_KEY = new TaskCompositeKey("chatId", "name");

    private final static TaskCompositeKey ALTERED_CHAT_ID = new TaskCompositeKey("dItahc", "name");

    private final static TaskCompositeKey ALTERED_NAME = new TaskCompositeKey("chatId", "eman");

    @Test
    public void equalsShouldBeTrueIfSame() {
        assertEquals(KEY, SAME_KEY);
    }

    @Test
    public void equalsShouldBeFalseIfNotSame() {
        assertNotEquals(KEY, ALTERED_CHAT_ID);
        assertNotEquals(KEY, ALTERED_NAME);
    }
}
