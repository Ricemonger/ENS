package app.security.user.service.ens_user.db;

import app.security.user.service.ens_user.EnsUserDetails;
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
}
