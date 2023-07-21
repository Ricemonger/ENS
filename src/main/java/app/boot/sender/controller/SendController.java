package app.boot.sender.controller;

import app.boot.security.JwtUtil;
import app.boot.sender.controller.dto.SendOneRequest;
import app.boot.sender.controller.exceptions.SenderApiException;
import app.boot.sender.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send")
@RequiredArgsConstructor
public class SendController {

    private final SendService sendService;

    private final JwtUtil jwtUtil;
    @PostMapping("/one")
    public void sendOne(@RequestHeader(name="Authorization") String token, @RequestBody SendOneRequest request){
        sendService.sendOne(jwtUtil.extractUsername(token), request.method(), request.contactId(),request.notificationText());
    }
    @PostMapping("/all")
    public void sendAll(@RequestHeader(name="Authorization") String token){
        sendService.sendAll(jwtUtil.extractUsername(token));
    }
    @ExceptionHandler(SenderApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String senderApiException(SenderApiException e){
        return e.getMessage();
    }
}

