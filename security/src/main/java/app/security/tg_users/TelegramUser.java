package app.security.tg_users;

import app.security.abstract_users.AbstractUser;
import app.security.tg_users.model.db.TelegramUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser extends AbstractUser {

    private TelegramUserEntity telegramUserEntity = new TelegramUserEntity();

    public TelegramUser(String chatId) {
        telegramUserEntity.setChatId(chatId);
    }

    public TelegramUser(String accountId, String chatId) {
        telegramUserEntity.setAccountId(accountId);
        telegramUserEntity.setChatId(chatId);
    }

    public void setAccountId(String accountId) {
        telegramUserEntity.setAccountId(accountId);
    }

    public String getAccountId() {
        return telegramUserEntity.getAccountId();
    }

    public void setChatId(String chatId) {
        telegramUserEntity.setChatId(chatId);
    }

    public String getChatId() {
        return telegramUserEntity.getChatId();
    }
}
