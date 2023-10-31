package app.security.user.service.any_user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnyUserDetailsService implements UserDetailsService {

    private final AnyUserRepository anyUserRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        return new AnyUserDetails(anyUserRepository.findById(accountId).orElseThrow());
    }
}
