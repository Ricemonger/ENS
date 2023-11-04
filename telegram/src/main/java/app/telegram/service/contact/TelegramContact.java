package app.telegram.service.contact;

import app.utils.contact.Method;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramContact {
    private Long chatId;
    private Method method;
    private String contactId;
    private String notificationName;
}
