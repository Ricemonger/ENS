package app.telegram.users.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

            return chatIdEq && tgTokEq && tgTokExpEq && scTokEq && scTokExpEq;

        } else {
            return false;
        }
    }
}
