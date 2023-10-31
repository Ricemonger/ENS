package app.notification.service.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationCompositeKey implements Serializable {
    private String accountId;
    private String name;
}
