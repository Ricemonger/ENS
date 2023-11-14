package app.security.tg_users.model.db;

import app.security.any_users.model.db.AnyUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tg-users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUserEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private AnyUserEntity anyUserEntity = new AnyUserEntity();

    @Id
    private String chatId;

    public TelegramUserEntity(String chatId) {
        this.chatId = chatId;
    }

    public TelegramUserEntity(String accountId, String chatId) {
        anyUserEntity.setAccountId(accountId);
        this.chatId = chatId;
    }

    public void setAccountId(String accountId) {
        anyUserEntity.setAccountId(accountId);
    }

    public String getAccountId() {
        return anyUserEntity.getAccountId();
    }
}
