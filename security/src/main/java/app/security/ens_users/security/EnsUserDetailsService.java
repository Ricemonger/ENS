package app.security.ens_users.security;

import app.security.ens_users.db.EnsUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnsUserDetailsService implements UserDetailsService {

    private final EnsUserRepository ensUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new EnsUserDetails(ensUserRepository.findById(username).orElseThrow());
    }

    public Object loadUserByAccountId(String accountId) throws UsernameNotFoundException {
        return new EnsUserDetails(ensUserRepository.findByAccountId(accountId).orElseThrow());
    }
}
