package app.send.controller;

import app.send.model.SendService;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendControllerService {

    private final SendService sendService;

    public void sendOne(String securityToken, SendOneRequest request) {
        log.trace("sendOne method was called with params: token-{}, request-{}", securityToken, request);
        sendService.sendOne(securityToken, request.method(), request.contactId(), request.notificationText());
    }

    public void sendAll(String securityToken) {
        log.trace("sendAll method was called with params: token-{}", securityToken);
        sendService.sendAll(securityToken);
    }
}
