package app.send.controller;

import app.utils.ExceptionMessage;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.sender.dto.SendManyRequest;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import app.utils.feign_clients.sender.exceptions.SenderApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("${application.config.request-mappings.sender}")
@RequiredArgsConstructor
public class SendController {

    private final SendControllerService service;

    @PostMapping("/one")
    @ResponseStatus(HttpStatus.OK)
    public void sendOne(@RequestHeader(name = "Authorization") String securityToken, @RequestBody SendOneRequest request) {
        service.sendOne(securityToken, request);
    }

    @PostMapping("/many")
    @ResponseStatus(HttpStatus.OK)
    public void sendMany(@RequestHeader(name = "Authorization") String securityToken, @RequestBody SendManyRequest request) {
        service.sendMany(securityToken, request);
    }

    @PostMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public void sendAll(@RequestHeader(name = "Authorization") String securityToken) {
        service.sendAll(securityToken);
    }

    @ExceptionHandler(SenderApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage senderApiException(SenderApiException e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Exception was thrown during sending operation");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionMessage illegalArgumentException(IllegalArgumentException e) {
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Wrong Method Name! Valid method names are:" + Arrays.toString(Method.values()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown exception was thrown");
    }
}

