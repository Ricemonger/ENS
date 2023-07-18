package app.boot.security.user.service;

import app.boot.security.user.controller.InvalidPasswordException;
import app.boot.security.user.controller.InvalidUsernameException;
import app.boot.security.JwtUtil;
import app.boot.security.user.controller.UserAlreadyExistsException;
import app.boot.security.user.model.User;
import app.boot.security.user.model.UserDetails;
import app.boot.security.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public String register(User user) {
        validateUsername(user.getUsername());
        validatePassword(user.getUsername());
        try {
            userRepository.findById(user.getUsername()).orElseThrow();
        }catch(NoSuchElementException e){
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            user.setPassword(password);
            return login(user);
        }
        throw new UserAlreadyExistsException();
    }
    public User getByUsername(String username) {
        return userRepository.findById(username).orElseThrow();
    }
    public String login(User user) {
        String username = user.getUsername();
        validateUsername(username);
        String password = user.getPassword();
        validatePassword(password);
        userRepository.findById(username).orElseThrow();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        return jwtUtil.generateToken(new UserDetails(new User(username,password)));
    }
    private void validateUsername(String username){
        String regex = ".*\\W+.*";
        if(username.length()<6 || username.length()>24 || username.matches(regex))
            throw new InvalidUsernameException();
    }
    private void validatePassword(String password){
        String regex = ".*[\\{\\}\\[\\]\\(\\):;'\".,<>/|\\\s]+.*";
        if(password.length()<6 || password.length()>16 || password.matches(regex))
            throw new InvalidPasswordException();
    }
}
