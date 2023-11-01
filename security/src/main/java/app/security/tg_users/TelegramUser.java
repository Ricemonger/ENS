package app.security.tg_users;

import app.security.any_users.AnyUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tg-users")
@Data
@Builder
@NoArgsConstructor
public class TelegramUser extends AnyUser {

    @Id
    private String chatId;

    public TelegramUser(String chatId) {
        this.chatId = chatId;
    }

}
