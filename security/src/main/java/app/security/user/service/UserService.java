package app.security.user.service;

import app.security.user.controller.exceptions.InvalidPasswordException;
import app.security.user.controller.exceptions.InvalidUsernameException;
import app.security.config.JwtUtil;
import app.security.user.controller.exceptions.UserAlreadyExistsException;
import app.security.user.controller.exceptions.UserDoesntExistException;
import app.security.user.model.User;
import app.security.user.model.UserDetails;
import app.security.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public String register(User user) {
        validateUsername(user.getUsername());
        validatePassword(user.getUsername());
        try {
            getById(user.getUsername());
        }catch(UserDoesntExistException e){
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            user.setPassword(password);
            log.trace("register method was executed with params:{}",user);
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
        getById(username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        String result = jwtUtil.generateToken(new UserDetails(new User(username,password)));
        log.trace("login method was executed with params: username-{}, password-{} and result: {}",username,passwordEncoder.encode(password),result);
        return result;
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
    private User getById(String username){
        try {
            return userRepository.findById(username).orElseThrow();
        }catch(NoSuchElementException e){
            throw new UserDoesntExistException(e);
        }
    }
}