package app.send.controller;

import app.send.model.SendService;
import app.utils.services.sender.dto.SendManyRequest;
import app.utils.services.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendControllerService {

    private final SendService sendService;

    public void sendOne(String securityToken, SendOneRequest request) {
        sendService.sendOne(securityToken, request.method(), request.contactId(), request.notificationText());
    }

    public void sendMany(String securityToken, SendManyRequest request) {
        sendService.sendMany(securityToken, request.method(), request.contactId(), request.notificationName());
    }

    public void sendAll(String securityToken) {
        sendService.sendAll(securityToken);
    }
}
