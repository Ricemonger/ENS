package app.notification.model;

import app.notification.model.db.NotificationCompositeKey;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NotificationCompositeKey.class)
public class Notification {
    @Id
    @JoinColumn
    private String accountId;
    @Id
    private String name;

    private String text;

    public Notification(String accountId, String name) {
        this.accountId = accountId;
        this.name = name;
        this.text = "";
    }
}
