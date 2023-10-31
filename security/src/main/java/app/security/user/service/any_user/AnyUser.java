package app.security.user.service.any_user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnyUser extends AbstractUser {
    @Id
    @Override
    public String getAccountId() {
        return super.getAccountId();
    }
}