package app.telegram.users;


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
public class TelegramUser {

    @Id
    private String chatId;

    private String tempTelegramToken;

    @Column(columnDefinition = "TIMESTAMP")
    private Date tempTelegramTokenExpirationTime;

    private String tempSecurityToken;

    @Column(columnDefinition = "TIMESTAMP")
    private Date tempSecurityTokenExpirationTime;

    public TelegramUser(String chatId) {
        this.chatId = chatId;
    }
}
