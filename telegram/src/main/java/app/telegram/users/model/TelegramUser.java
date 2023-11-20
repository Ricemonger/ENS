package app.telegram.users.model;


import app.telegram.users.model.db.TelegramUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser {

    private TelegramUserEntity entity = new TelegramUserEntity();

    public TelegramUser(String chatId) {
        entity.setChatId(chatId);
    }

    public TelegramUser(String chatId, String telegramToken, Date telegramTokenExpiration, String securityToken,
                        Date securityTokenExpiration) {
        entity.setChatId(chatId);
        entity.setTempTelegramToken(telegramToken);
        entity.setTempTelegramTokenExpirationTime(telegramTokenExpiration);
        entity.setTempSecurityToken(securityToken);
        entity.setTempSecurityTokenExpirationTime(securityTokenExpiration);
    }

    public void setChatId(String chatId) {
        entity.setChatId(chatId);
    }

    public String getChatId() {
        return entity.getChatId();
    }

    public void setTempTelegramToken(String tempTelegramToken) {
        entity.setTempTelegramToken(tempTelegramToken);
    }

    public String getTempTelegramToken() {
        return entity.getTempTelegramToken();
    }

    public void setTempTelegramTokenExpirationTime(Date tempTelegramTokenExpirationTime) {
        entity.setTempTelegramTokenExpirationTime(tempTelegramTokenExpirationTime);
    }

    public Date getTempTelegramTokenExpirationTime() {
        return entity.getTempTelegramTokenExpirationTime();
    }

    public void setTempSecurityToken(String tempSecurityToken) {
        entity.setTempSecurityToken(tempSecurityToken);
    }

    public String getTempSecurityToken() {
        return entity.getTempSecurityToken();
    }

    public void setTempSecurityTokenExpirationTime(Date tempSecurityTokenExpirationTime) {
        entity.setTempSecurityTokenExpirationTime(tempSecurityTokenExpirationTime);
    }

    public Date getTempSecurityTokenExpirationTime() {
        return entity.getTempSecurityTokenExpirationTime();
    }

}
