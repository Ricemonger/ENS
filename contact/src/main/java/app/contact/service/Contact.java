package app.contact.service;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "contact")
@NoArgsConstructor
@Data
@Builder
@IdClass(ContactCompositeKey.class)
public class Contact {
    @Id
    @JoinColumn
    private String accountId;
    @Id
    @Enumerated(EnumType.STRING)
    private Method method;
    @Id
    private String contactId;

    private String notificationName;

    public Contact(String accountId, Method method, String contactId) {
        this(accountId, method, contactId, "");
    }

    public Contact(String accountId, Method method, String contactId, String notificationName) {
        this.accountId = accountId;
        this.method = method;
        this.contactId = contactId;
        this.notificationName = Objects.requireNonNullElse(notificationName, "");
    }
}
