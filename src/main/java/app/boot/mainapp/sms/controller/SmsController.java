package app.boot.mainapp.sms.controller;

import app.boot.mainapp.sms.dto.SmsOneRequest;
import app.boot.mainapp.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/one")
    public void sendOne(@RequestHeader(name="Authorization") String token, @RequestBody SmsOneRequest request){
        smsService.sendOne(
                token,
                request.phoneNumber(),
                request.notificationName()
        );
    }
    @PostMapping("/all")
    public void sendAll(@RequestHeader(name="Authorization") String token){
        smsService.sendAll(token);
    }
}
