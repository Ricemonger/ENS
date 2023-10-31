package app.security.user.service.ens_user;

import app.security.user.service.any_user.AnyUser;
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
public class EnsUser extends AnyUser {
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
