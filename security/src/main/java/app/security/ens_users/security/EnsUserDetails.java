package app.security.ens_users.security;

import app.security.abstract_users.AbstractUser;
import app.security.abstract_users.security.AbstractUserDetails;
import app.security.ens_users.EnsUser;

public class EnsUserDetails extends AbstractUserDetails {

    public EnsUserDetails(AbstractUser abstractUser) {
        super(abstractUser);
    }

    @Override
    public String getUsername() {
        return getUser().getUsername();
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public EnsUser getUser() {
        return (EnsUser) super.getUser();
    }
}
