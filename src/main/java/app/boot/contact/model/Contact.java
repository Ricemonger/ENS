package app.boot.contact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Entity
@NoArgsConstructor
@Data
@Builder
@IdClass(ContactCompositeKey.class)
public class Contact {
    @Id
    private String username;
    @Id
    @Enumerated(EnumType.STRING)
    private Method method;
    @Id
    private String contactId;

    private String notificationName;

    public Contact(String username, String method, String contactId) {
        this(username, method, contactId, "");
    }
    public Contact(String username, String method, String contactId, String notificationName){
        this(username,Method.valueOf(method.toUpperCase().trim()),contactId,notificationName);
    }
    public Contact(String username, Method method, String contactId, String notificationName){
        this.username = username.trim();
        this.method = method;
        this.contactId = contactId.trim();
        if (notificationName!=null) {
            this.notificationName = notificationName.trim();
        }
    }
    public void setUsername(String username) {
        this.username = username.trim();
    }
    public void setContactId(String contactId) {
        this.contactId = contactId.trim();
    }
    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName.trim();
    }
}
