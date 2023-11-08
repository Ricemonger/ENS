package app.send.controller;

import app.send.controller.dto.SendOneRequest;
import app.send.exceptions.SenderApiException;
import app.utils.ExceptionMessage;
import app.utils.feign_clients.contact.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/send")
@RequiredArgsConstructor
@Slf4j
public class SendController {

    private final SendControllerService service;

    @PostMapping("/one")
    @ResponseStatus(HttpStatus.OK)
    public void sendOne(@RequestHeader(name = "Authorization") String securityToken, @RequestBody SendOneRequest request) {
        service.sendOne(securityToken, request);
    }

    @PostMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public void sendAll(@RequestHeader(name = "Authorization") String securityToken) {
        service.sendAll(securityToken);
    }

    @ExceptionHandler(SenderApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage senderApiException(SenderApiException e) {
        log.warn("SenderApiException was thrown with message: {}", e.getMessage());
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Exception was thrown during sending operation", e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionMessage illegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException occurred: {}" + e.getMessage());
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Wrong Method Name! Valid method names are:" + Arrays.toString(Method.values()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        log.warn("UnknownException occurred: {}" + e.getMessage());
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown exception was thrown", e);
    }
}

