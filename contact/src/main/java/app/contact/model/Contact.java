package app.contact.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Contact(String username, Method method, String contactId) {
        this(username, method, contactId, "");
    }

    public Contact(String username, Method method, String contactId, String notificationName){
        this.username = username;
        this.method = method;
        this.contactId = contactId;
        if (notificationName==null) {
            notificationName = "";
        }
        else {
            this.notificationName = notificationName;
        }
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

}
