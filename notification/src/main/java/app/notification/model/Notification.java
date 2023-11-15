package app.notification.model;

import app.notification.model.db.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private NotificationEntity entity = new NotificationEntity();

    public Notification(String accountId, String name) {
        this(accountId, name, "");
    }

    public Notification(String accountId, String name, String text) {
        entity.setAccountId(accountId);
        entity.setName(name);
        entity.setText(text);
    }

    public void setAccountId(String accountId) {
        entity.setAccountId(accountId);
    }

    public String getAccountId() {
        return entity.getAccountId();
    }

    public void setName(String name) {
        entity.setName(name);
    }

    public String getName() {
        return entity.getName();
    }

    public void setText(String text) {
        entity.setText(text);
    }

    public String getText() {
        return entity.getText();
    }
}
