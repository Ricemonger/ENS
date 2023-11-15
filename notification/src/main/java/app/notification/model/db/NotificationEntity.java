package app.notification.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NotificationCompositeKey.class)
public class NotificationEntity {
    @Id
    @JoinColumn
    private String accountId;
    @Id
    private String name;

    private String text;

    public NotificationEntity(String accountId, String name) {
        this.accountId = accountId;
        this.name = name;
        this.text = "";
    }
}
