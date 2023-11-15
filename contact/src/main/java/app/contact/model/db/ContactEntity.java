package app.contact.model.db;

import app.contact.model.Method;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "contacts")
@NoArgsConstructor
@Data
@Builder
@IdClass(ContactCompositeKey.class)
public class ContactEntity {
    @Id
    private String accountId;
    @Id
    @Enumerated(EnumType.STRING)
    private Method method;
    @Id
    private String contactId;

    private String notificationName;

    public ContactEntity(String accountId, Method method, String contactId) {
        this(accountId, method, contactId, "");
    }

    public ContactEntity(String accountId, Method method, String contactId, String notificationName) {
        this.accountId = accountId;
        this.method = method;
        this.contactId = contactId;
        this.notificationName = Objects.requireNonNullElse(notificationName, "");
    }
}
