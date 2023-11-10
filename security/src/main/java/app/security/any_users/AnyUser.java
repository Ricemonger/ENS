package app.security.any_users;

import app.security.abstract_users.AbstractUser;
import app.security.any_users.model.db.AnyUserEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class AnyUser extends AbstractUser {

    private AnyUserEntity anyUserEntity = new AnyUserEntity();

    public AnyUser(String accountId) {
        anyUserEntity = new AnyUserEntity(accountId);
    }

    public String getAccountId() {
        return anyUserEntity.getAccountId();
    }

    public void setAccountId(String accountId) {
        anyUserEntity.setAccountId(accountId);
    }
}