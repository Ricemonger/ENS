package app.security.config;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utils.ExceptionMessage;


@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JwtUtil jwtUtil;

    @GetMapping("/getUsername")
    public String getUsername(@RequestHeader(name="Authorization") String token){
        log.trace("getUsername method was called with token-{}",token);
        String result = jwtUtil.extractUsername(token);
        log.trace("getUsername method's with token-{} result is {}",token,result);
        return result;
    }
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ExceptionMessage jwtException(JwtException e){
        log.warn("JwtException occurred: {}",e.getMessage());
        return new ExceptionMessage(HttpStatus.BAD_REQUEST,"Invalid or expired jwt token provided",e);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e){
        log.warn("UnknownException occurred: {}",e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR,e);
    }
}
