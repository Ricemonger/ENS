package app.security.any_users;

import app.security.abstract_users.AbstractUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class AnyUser extends AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Override
    public String getAccountId() {
        return super.getAccountId();
    }

    public AnyUser(String accountId) {
        setAccountId(accountId);
    }
}