package app.security.abstract_users.controller;

import app.utils.ExceptionMessage;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${application.config.request-mappings.abstract}")
@RequiredArgsConstructor
public class AbstractUserController {

    private final AbstractUserControllerService abstractUserControllerService;

    @GetMapping("/getAccountId")
    @ResponseStatus(HttpStatus.OK)
    public String getAccountId(@RequestHeader(name = "Authorization") String securityToken) {
        return abstractUserControllerService.getAccountId(securityToken);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ExceptionMessage jwtRuntimeException(JwtException e) {
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid or expired jwt token provided");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN EXCEPTION");
    }
}
