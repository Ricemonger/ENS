package app.send.controller;

import app.send.controller.dto.SendOneRequest;
import app.send.controller.exceptions.SenderApiException;
import app.send.service.SendService;
import app.send.service.contact.Method;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utils.ExceptionMessage;
import utils.JwtClient;

import java.util.Arrays;

@RestController
@RequestMapping("/api/send")
@RequiredArgsConstructor
public class SendController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SendService sendService;

    private final JwtClient jwtUtil;
    @PostMapping("/one")
    public void sendOne(@RequestHeader(name="Authorization") String token, @RequestBody SendOneRequest request){
        String username = jwtUtil.extractUsername(token);
        log.trace("sendOne method was called with params: username-{}, request-{}",username,request);
        sendService.sendOne(token, username, request.method(), request.contactId(),request.notificationText());
    }
    @PostMapping("/all")
    public void sendAll(@RequestHeader(name="Authorization") String token){
        String username = jwtUtil.extractUsername(token);
        log.trace("sendAll method was called with params: username-{}",username);
        sendService.sendAll(token, username);
    }
    @ExceptionHandler(SenderApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage senderApiException(SenderApiException e){
        log.warn("SenderApiException was thrown with message: {}",e.getMessage());
        return new ExceptionMessage(HttpStatus.BAD_REQUEST,"Exception was thrown during sending operation",e);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionMessage illegalArgumentException(IllegalArgumentException e){
        log.warn("IllegalArgumentException occurred: {}" + e.getMessage());
        return new ExceptionMessage(HttpStatus.BAD_REQUEST,"Wrong Method Name! Valid method names are:" + Arrays.toString(Method.values()));
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e){
        log.warn("UnknownException occurred: {}" + e.getMessage());
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR,"Unknown exception was thrown",e);
    }
}

