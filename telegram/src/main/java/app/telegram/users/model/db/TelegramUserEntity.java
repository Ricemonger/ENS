package app.telegram.users.model.db;

import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "telegram")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private InputState inputState = InputState.BASE;

    @Enumerated(value = EnumType.STRING)
    private InputGroup inputGroup = InputGroup.BASE;

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

            boolean inputStateEq = this.inputState == entity.inputState;

            boolean inputGroupEq = this.inputGroup == entity.inputGroup;

            return chatIdEq && tgTokEq && tgTokExpEq && scTokEq && scTokExpEq && inputStateEq && inputGroupEq;

        } else {
            return false;
        }
    }
}
