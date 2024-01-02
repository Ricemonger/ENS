package app.telegram.task.model.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskCompositeKey implements Serializable {
    private String chatId;
    private String name;
}
