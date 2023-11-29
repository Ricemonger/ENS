package app.utils.feign_clients.sender;

import app.utils.feign_clients.sender.dto.SendManyRequest;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendFeignClientService {

    private final SendFeignClient sendFeignClient;

    public void sendOne(String securityToken, SendOneRequest request) {
        sendFeignClient.sendOne(securityToken, request);
    }

    public void sendMany(String securityToken, SendManyRequest request) {
        sendFeignClient.sendMany(securityToken, request);
    }

    public void sendAll(String securityToken) {
        sendFeignClient.sendAll(securityToken);
    }
}
