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
    private String accountId = "";

    @OneToOne(mappedBy = "anyUserEntity")
    private EnsUserEntity ensUserEntity;

    @OneToOne(mappedBy = "anyUserEntity")
    private TelegramUserEntity telegramUserEntity;

    public AnyUserEntity(String accountId) {
        this.accountId = accountId;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (o instanceof AnyUserEntity entity) {
            if (this.accountId != null) {
                return this.accountId.equals(entity.getAccountId());
            } else {
                return entity.getAccountId() == null;
            }
        }
        return false;
    }

    public String toString() {
        return String.format("(AnyUserEntity:accountId=%s)", accountId);
    }
}
