package app.security.user.service.ens_user;

import app.security.user.service.any_user.AbstractUser;
import app.security.user.service.any_user.AbstractUserDetails;

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
