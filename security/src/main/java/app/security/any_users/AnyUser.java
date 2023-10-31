package app.security.any_users;

import app.security.abstract_users.AbstractUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "any-users")
@Builder
@NoArgsConstructor
public class AnyUser extends AbstractUser {
    @Id
    @GeneratedValue
    @Override
    public String getAccountId() {
        return super.getAccountId();
    }
}