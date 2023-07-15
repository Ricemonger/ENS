package app.boot.notification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
    private String username;
    @Id
    private String name;
    private String text;
    public Notification(String username, String name){
        this.username = username;
        this.name = name;
        this.text = "";
    }
}
