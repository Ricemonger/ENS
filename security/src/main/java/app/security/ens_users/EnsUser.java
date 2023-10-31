package app.security.ens_users;

import app.security.abstract_users.AbstractUser;
import jakarta.persistence.*;
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
public class EnsUser extends AbstractUser {
    @Column(unique = true)
    @NonNull
    @JoinColumn
    private String accountId;
    @Id
    @NonNull
    private String username;
    @NonNull
    private String password;
}
