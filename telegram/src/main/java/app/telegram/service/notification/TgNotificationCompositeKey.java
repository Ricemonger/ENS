package app.telegram.service.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TgNotificationCompositeKey implements Serializable {
    private Long chatId;
    private String name;
}
