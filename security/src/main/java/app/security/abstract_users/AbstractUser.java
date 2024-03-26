package app.security.abstract_users;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractUser {

    public abstract String getAccountId();

    public abstract void setAccountId(String accountId);
}
