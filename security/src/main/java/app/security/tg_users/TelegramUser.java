package app.security.tg_users;

import app.security.abstract_users.AbstractUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tg-users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser extends AbstractUser {
    @NonNull
    @Column(unique = true)
    @JoinColumn
    private String accountId;
    @Id
    private String chatId;

}
