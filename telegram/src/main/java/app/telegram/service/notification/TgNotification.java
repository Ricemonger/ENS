package app.telegram.service.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tg-notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TgNotification {

    @Id
    private Long chatId;
    @Id
    private String name;

    private String text;

    public TgNotification(Long chatId, String name) {
        this.chatId = chatId;
        this.name = name;
        this.text = "";
    }
}
