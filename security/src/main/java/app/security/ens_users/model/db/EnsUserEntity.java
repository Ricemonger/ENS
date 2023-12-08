package app.security.ens_users.model.db;

import app.security.any_users.model.db.AnyUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ens-users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnsUserEntity {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private AnyUserEntity anyUserEntity = new AnyUserEntity();

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    public EnsUserEntity(String accountId, String username, String password) {
        anyUserEntity = new AnyUserEntity(accountId);
        this.username = username;
        this.password = password;
    }

    public EnsUserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getAccountId() {
        return anyUserEntity.getAccountId();
    }

    public void setAccountId(String accountId) {
        anyUserEntity.setAccountId(accountId);
    }
}
