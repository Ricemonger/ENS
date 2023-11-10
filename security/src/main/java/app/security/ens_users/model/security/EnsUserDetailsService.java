package app.security.ens_users.model.security;

import app.security.ens_users.model.db.EnsUserRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnsUserDetailsService implements UserDetailsService {

    private final EnsUserRepositoryService ensUserRepositoryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new EnsUserDetails(ensUserRepositoryService.findByIdOrThrow(username));
    }

    public Object loadUserByAccountId(String accountId) throws UsernameNotFoundException {
        return new EnsUserDetails(ensUserRepositoryService.findByAccountIdOrThrow(accountId));
    }
}
