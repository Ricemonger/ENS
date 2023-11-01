package app.security.abstract_users.controller;

import app.security.abstract_users.security.JwtUtil;
import app.utils.ExceptionMessage;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
@Slf4j
public class SecurityController {

    private final JwtUtil jwtUtil;

    @GetMapping("/getAccountId")
    @ResponseStatus(HttpStatus.OK)
    public String getAccountId(@RequestHeader(name = "Authorization") String token) {
        log.trace("getAccountId method was called with token-{}", token);
        String result = jwtUtil.extractAccountId(token);
        log.trace("getAccountId method's with token-{} result is {}", token, result);
        return result;
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ExceptionMessage jwtRuntimeException(JwtException e) {
        log.warn("JwtException occurred: {}", e.getMessage());
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid or expired jwt token provided", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        log.warn("UnknownException occurred: {}", e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
