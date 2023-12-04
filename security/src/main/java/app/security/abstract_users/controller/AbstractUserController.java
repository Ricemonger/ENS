package app.security.abstract_users.controller;

import app.utils.ExceptionMessage;
import app.utils.feign_clients.security_abstract.exceptions.InvalidSecurityTokenException;
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

    @ExceptionHandler(InvalidSecurityTokenException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ExceptionMessage jwtRuntimeException(InvalidSecurityTokenException e) {
        return new ExceptionMessage(HttpStatus.UNAUTHORIZED, "Invalid or expired jwt token, please get new token via /login or /register pages");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
    }
}
