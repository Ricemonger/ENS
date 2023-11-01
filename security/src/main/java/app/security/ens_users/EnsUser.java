package app.security.ens_users;

import app.security.any_users.AnyUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "ens-users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnsUser extends AnyUser {

    @Id
    private String username;

    @NonNull
    private String password;

    public EnsUser(String accountId, String username, String password) {
        super(accountId);
        this.username = username;
        this.password = password;
    }
}
