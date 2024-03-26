package app.telegram.users.model.db;

import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.telegram.users.model.TelegramUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "telegram")
@Data
@Builder
@NoArgsConstructor
public class TelegramUserEntity {

    @Id
    private String chatId;

    private String tempTelegramToken;

    @Column(columnDefinition = "TIMESTAMP")
    private Date tempTelegramTokenExpirationTime;

    private String tempSecurityToken;

    @Column(columnDefinition = "TIMESTAMP")
    private Date tempSecurityTokenExpirationTime;

    @Enumerated(value = EnumType.STRING)
    @Column()
    private InputState inputState = InputState.BASE;

    @Enumerated(value = EnumType.STRING)
    private InputGroup inputGroup = InputGroup.BASE;

    @Column(columnDefinition = "boolean default true")
    private Boolean settingActionConfirmationFlag = true;

    @Column(columnDefinition = "varchar(255) default 'sendall'")
    private String settingCustomPhrase = "sendall";

    public TelegramUserEntity(String chatId,
                              String tgToken, Date tgTokenExp,
                              String secToken, Date secTokenExp,
                              InputState inputState, InputGroup inputGroup,
                              boolean settingActionConfirmationFlag, String settingCustomPhrase) {
        this(chatId, tgToken, tgTokenExp, secToken, secTokenExp, inputState, inputGroup);
        this.settingActionConfirmationFlag = settingActionConfirmationFlag;
        this.settingCustomPhrase = settingCustomPhrase;
    }

    public TelegramUserEntity(String chatId,
                              String tgToken, Date tgTokenExp,
                              String secToken, Date secTokenExp,
                              InputState inputState, InputGroup inputGroup) {
        this(chatId, tgToken, tgTokenExp, secToken, secTokenExp);
        this.inputState = inputState;
        this.inputGroup = inputGroup;
    }

    public TelegramUserEntity(String chatId, String tgToken, Date tgTokenExp, String secToken, Date secTokenExp) {
        this(chatId);
        this.tempTelegramToken = tgToken;
        this.tempTelegramTokenExpirationTime = tgTokenExp;
        this.tempSecurityToken = secToken;
        this.tempSecurityTokenExpirationTime = secTokenExp;
    }

    public TelegramUserEntity(String chatId) {
        this.chatId = chatId;
    }

    public TelegramUserEntity(TelegramUser telegramUser) {
        this.chatId = telegramUser.getChatId();
        this.tempTelegramToken = telegramUser.getTempTelegramToken();
        this.tempTelegramTokenExpirationTime = telegramUser.getTempTelegramTokenExpirationTime();
        this.tempSecurityToken = telegramUser.getTempSecurityToken();
        this.tempSecurityTokenExpirationTime = telegramUser.getTempSecurityTokenExpirationTime();
        this.inputState = telegramUser.getInputState();
        this.inputGroup = telegramUser.getInputGroup();
        this.settingActionConfirmationFlag = telegramUser.isActionConfirmationFlag();
        this.settingCustomPhrase = telegramUser.getCustomPhrase();
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof TelegramUserEntity entity) {

            boolean chatIdEq = this.chatId.equals(entity.getChatId());

            boolean tgTokEq;
            if (this.tempTelegramToken == null) {
                tgTokEq = entity.getTempTelegramToken() == null;
            } else {
                tgTokEq = this.tempTelegramToken.equals(entity.getTempTelegramToken());
            }

            boolean tgTokExpEq;
            if (this.tempTelegramTokenExpirationTime == null && entity.tempTelegramTokenExpirationTime == null) {
                tgTokExpEq = true;
            } else if (this.tempTelegramTokenExpirationTime == null || entity.tempTelegramTokenExpirationTime == null) {
                tgTokExpEq = false;
            } else {
                tgTokExpEq =
                        this.tempTelegramTokenExpirationTime.getTime() == entity.tempTelegramTokenExpirationTime.getTime();
            }

            boolean scTokEq;
            if (this.tempSecurityToken == null) {
                scTokEq = entity.tempSecurityToken == null;
            } else {
                scTokEq = this.tempSecurityToken.equals(entity.getTempSecurityToken());
            }

            boolean scTokExpEq;
            if (this.tempSecurityTokenExpirationTime == null && entity.tempSecurityTokenExpirationTime == null) {
                scTokExpEq = true;
            } else if (this.tempSecurityTokenExpirationTime == null || entity.tempSecurityTokenExpirationTime == null) {
                scTokExpEq = false;
            } else {
                scTokExpEq =
                        this.tempSecurityTokenExpirationTime.getTime() == entity.tempSecurityTokenExpirationTime.getTime();
            }

            boolean inpStEq = this.inputState == entity.inputState;

            boolean inpGrEq = this.inputGroup == entity.inputGroup;

            boolean acConEq = this.settingActionConfirmationFlag == entity.settingActionConfirmationFlag;

            boolean custPhrEq;

            if (this.settingCustomPhrase == null && entity.settingCustomPhrase == null) {
                custPhrEq = true;
            } else if (this.settingCustomPhrase == null || entity.settingCustomPhrase == null) {
                custPhrEq = false;
            } else {
                custPhrEq = this.settingCustomPhrase.equals(entity.settingCustomPhrase);
            }

            return chatIdEq && tgTokEq && tgTokExpEq && scTokEq && scTokExpEq && inpStEq && inpGrEq && acConEq && custPhrEq;

        } else {
            return false;
        }
    }
}
