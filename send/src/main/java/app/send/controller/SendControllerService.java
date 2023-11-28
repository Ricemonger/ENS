package app.send.controller;

import app.send.model.SendService;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendControllerService {

    private final SendService sendService;

    public void sendOne(String securityToken, SendOneRequest request) {
        sendService.sendOne(securityToken, request.method(), request.contactId(), request.notificationText());
    }

    public void sendAll(String securityToken) {
        sendService.sendAll(securityToken);
    }
}
