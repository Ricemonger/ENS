package app.boot.sender.controller;

import app.boot.security.JwtUtil;
import app.boot.sender.controller.dto.SendOneRequest;
import app.boot.sender.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send")
@RequiredArgsConstructor
public class SendController {

    private final SendService sendService;

    private final JwtUtil jwtUtil;
    @PostMapping("/one")
    public void sendOne(@RequestHeader(name="Authorization") String token, @RequestBody SendOneRequest request){
        sendService.sendOne(jwtUtil.extractUsername(token), request.method(), request.contactId());
    }
    @PostMapping("/all")
    public void sendAll(@RequestHeader(name="Authorization") String token){
        sendService.sendAll(jwtUtil.extractUsername(token));
    }
}

