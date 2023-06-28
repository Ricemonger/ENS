package app.boot.mainapp.contact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
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
        this.username = username;
        this.method = Method.valueOf(method.toUpperCase());
        this.contactId = contactId;
        this.notificationName = "";
    }
    public Contact(String username, String method, String contactId, String notificationName){
        this(username,method,contactId);
        this.notificationName = notificationName;
    }
}
