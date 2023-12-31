package app.telegram.users.model;


import app.telegram.users.model.db.TelegramUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class TelegramUser {

    private TelegramUserEntity entity = new TelegramUserEntity();

    public TelegramUser(String chatId,
                        String tgToken, Date tgTokenExp,
                        String secToken, Date secTokenExp,
                        InputState inputState, InputGroup inputGroup,
                        boolean settingActionConfirmationFlag, String customPhrase) {
        this(chatId, tgToken, tgTokenExp, secToken, secTokenExp, inputState, inputGroup);
        entity.setSettingActionConfirmationFlag(settingActionConfirmationFlag);
        entity.setSettingCustomPhrase(customPhrase);
    }

    public TelegramUser(String chatId,
                        String tgToken, Date tgTokenExp,
                        String secToken, Date secTokenExp,
                        InputState inputState, InputGroup inputGroup) {
        this(chatId, tgToken, tgTokenExp, secToken, secTokenExp);
        entity.setInputState(inputState);
        entity.setInputGroup(inputGroup);
    }

    public TelegramUser(String chatId, String telegramToken, Date telegramTokenExpiration, String securityToken,
                        Date securityTokenExpiration) {
        this(chatId);
        entity.setTempTelegramToken(telegramToken);
        entity.setTempTelegramTokenExpirationTime(telegramTokenExpiration);
        entity.setTempSecurityToken(securityToken);
        entity.setTempSecurityTokenExpirationTime(securityTokenExpiration);
    }

    public TelegramUser(String chatId, InputState inputState, InputGroup inputGroup) {
        this(chatId);
        entity.setInputState(inputState);
        entity.setInputGroup(inputGroup);
    }

    public TelegramUser(String chatId) {
        entity.setChatId(chatId);
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

    public InputState getInputState() {
        return entity.getInputState();
    }

    public void setInputState(InputState inputState) {
        entity.setInputState(inputState);
    }

    public InputGroup getInputGroup() {
        return entity.getInputGroup();
    }

    public void setInputGroup(InputGroup inputGroup) {
        entity.setInputGroup(inputGroup);
    }

    public boolean isActionConfirmationFlag() {
        return entity.getSettingActionConfirmationFlag();
    }

    public void setActionConfirmationFlag(boolean flag) {
        entity.setSettingActionConfirmationFlag(flag);
    }

    public String getCustomPhrase() {
        return entity.getSettingCustomPhrase();
    }

    public void setCustomPhrase(String customPhrase) {
        entity.setSettingCustomPhrase(customPhrase);
    }

}
