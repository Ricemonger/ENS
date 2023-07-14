package app.boot.authentication.user.service;

import app.boot.authentication.user.model.UserDetails;
import app.boot.authentication.security.JwtUtil;
import app.boot.authentication.user.service.repository.UserRepository;
import app.boot.authentication.user.controller.UserAlreadyExistsException;
import app.boot.authentication.user.model.User;
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
        String password = user.getPassword();
        userRepository.findById(username).orElseThrow();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        return jwtUtil.generateToken(new UserDetails(new User(username,password)));
    }
}
