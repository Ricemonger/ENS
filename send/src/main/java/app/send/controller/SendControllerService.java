package app.send.controller;

import app.send.controller.dto.SendOneRequest;
import app.send.model.SendService;
import app.utils.JwtClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendControllerService {

    private final SendService sendService;

    private final JwtClient jwtUtil;

    public void sendOne(String securityToken, SendOneRequest request) {
        String username = jwtUtil.extractAccountId(securityToken);
        log.trace("sendOne method was called with params: username-{}, request-{}", username, request);
        sendService.sendOne(securityToken, username, request.method(), request.contactId(), request.notificationText());
    }

    public void sendAll(String securityToken) {
        String username = jwtUtil.extractAccountId(securityToken);
        log.trace("sendAll method was called with params: username-{}", username);
        sendService.sendAll(securityToken, username);
    }
}
