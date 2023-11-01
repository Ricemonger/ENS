package app.security.any_users;

import app.security.abstract_users.AbstractUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnyUser extends AbstractUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
}