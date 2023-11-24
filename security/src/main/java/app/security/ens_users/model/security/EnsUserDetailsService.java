package app.security.ens_users.model.security;

import app.security.ens_users.model.db.EnsUserRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnsUserDetailsService implements UserDetailsService {

    private final EnsUserRepositoryService ensUserRepositoryService;

    @Override
    public EnsUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.trace("loadByUsername was called with username-{}", username);
        return new EnsUserDetails(ensUserRepositoryService.findByIdOrThrow(username));
    }

    public EnsUserDetails loadUserByAccountId(String accountId) {
        log.trace("loadByAccountId was called with accountId-{}", accountId);
        return new EnsUserDetails(ensUserRepositoryService.findByAccountIdOrThrow(accountId));
    }
}
