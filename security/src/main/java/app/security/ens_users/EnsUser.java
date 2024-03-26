package app.security.ens_users;

import app.security.abstract_users.AbstractUser;
import app.security.ens_users.model.db.EnsUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EnsUser extends AbstractUser {

    private final EnsUserEntity ensUserEntity = new EnsUserEntity();

    public EnsUser(String username, String password) {
        this(null, username, password);
    }

    public EnsUser(String accountId, String username, String password) {
        ensUserEntity.setAccountId(accountId);
        ensUserEntity.setUsername(username);
        ensUserEntity.setPassword(password);
    }

    @Override
    public String getAccountId() {
        return ensUserEntity.getAccountId();
    }

    @Override
    public void setAccountId(String accountId) {
        ensUserEntity.setAccountId(accountId);
    }

    public void setUsername(String username) {
        ensUserEntity.setUsername(username);
    }

    public String getUsername() {
        return ensUserEntity.getUsername();
    }

    public void setPassword(String password) {
        ensUserEntity.setPassword(password);
    }

    public String getPassword() {
        return ensUserEntity.getPassword();
    }
}
