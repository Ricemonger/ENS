package app.contact.model;

import app.contact.model.db.ContactEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    private ContactEntity entity = new ContactEntity();

    public Contact(String accountId, Method method, String contactId) {
        this(accountId, method, contactId, "");
    }

    public Contact(String accountId, Method method, String contactId, String notificationName) {
        entity.setAccountId(accountId);
        entity.setMethod(method);
        entity.setContactId(contactId);
        entity.setNotificationName(Objects.requireNonNullElse(notificationName, ""));
    }

    public void setAccountId(String accountId) {
        entity.setAccountId(accountId);
    }

    public String getAccountId() {
        return entity.getAccountId();
    }

    public void setMethod(Method method) {
        entity.setMethod(method);
    }

    public Method getMethod() {
        return entity.getMethod();
    }

    public void setContactId(String contactId) {
        entity.setContactId(contactId);
    }

    public String getContactId() {
        return entity.getContactId();
    }

    public void setNotificationName(String notificationName) {
        entity.setNotificationName(notificationName);
    }

    public String getNotificationName() {
        return entity.getNotificationName();
    }
}
