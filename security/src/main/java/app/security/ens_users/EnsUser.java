package app.security.ens_users;

import app.security.abstract_users.AbstractUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ens-users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnsUser extends AbstractUser {

    @Column(unique = true, nullable = false)
    private String accountId;

    @Id
    private String username;

    @Column(nullable = false)
    private String password;
}
