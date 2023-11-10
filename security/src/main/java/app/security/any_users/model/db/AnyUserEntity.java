package app.security.any_users.model.db;

import app.security.ens_users.model.db.EnsUserEntity;
import app.security.tg_users.model.db.TelegramUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnyUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private String accountId;

    @OneToOne(mappedBy = "anyUserEntity")
    private EnsUserEntity ensUserEntity;

    @OneToOne(mappedBy = "anyUserEntity")
    private TelegramUserEntity telegramUserEntity;

    public AnyUserEntity(String accountId) {
        this.accountId = accountId;
    }
}
