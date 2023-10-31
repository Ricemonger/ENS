package app.security.abstract_users.security;

import app.security.abstract_users.AbstractUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractUserDetails implements UserDetails {

    private final AbstractUser abstractUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public abstract String getUsername();

    @Override
    public abstract String getPassword();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractUserDetails that)) return false;
        return abstractUser.equals(that.abstractUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractUser);
    }

    public String getAccountId() {
        return abstractUser.getAccountId();
    }

    public AbstractUser getUser() {
        return abstractUser;
    }
}