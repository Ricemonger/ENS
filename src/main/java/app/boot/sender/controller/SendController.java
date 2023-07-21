package app.boot.sender.controller;

import app.boot.security.JwtUtil;
import app.boot.sender.controller.dto.SendOneRequest;
import app.boot.sender.controller.exceptions.SenderApiException;
import app.boot.sender.service.SendService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send")
@RequiredArgsConstructor
public class SendController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SendService sendService;

    private final JwtUtil jwtUtil;
    @PostMapping("/one")
    public void sendOne(@RequestHeader(name="Authorization") String token, @RequestBody SendOneRequest request){
        String username = jwtUtil.extractUsername(token);
        log.trace("sendOne method was called with params: username-{}, request-{}",username,request);
        sendService.sendOne(username, request.method(), request.contactId(),request.notificationText());
    }
    @PostMapping("/all")
    public void sendAll(@RequestHeader(name="Authorization") String token){
        String username = jwtUtil.extractUsername(token);
        log.trace("sendAll method was called with params: username-{}",username);
        sendService.sendAll(username);
    }
    @ExceptionHandler(SenderApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String senderApiException(SenderApiException e){
        log.warn("SenderApiException was thrown with message: {}",e.getMessage());
        return e.getMessage();
    }
}

