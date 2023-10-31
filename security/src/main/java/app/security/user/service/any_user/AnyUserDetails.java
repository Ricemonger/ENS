package app.security.user.service.any_user;

public class AnyUserDetails extends AbstractUserDetails {

    public AnyUserDetails(AnyUser anyUser) {
        super(anyUser);
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return getUser().getAccountId();
    }

    @Override
    public AnyUser getUser() {
        return (AnyUser) super.getUser();
    }
}

