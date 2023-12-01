package app.utils.feign_clients.sender;

import app.utils.feign_clients.sender.dto.SendManyRequest;
import app.utils.feign_clients.sender.dto.SendOneRequest;

public class MockSendFeignClient implements SendFeignClient {
    @Override
    public void sendOne(String securityToken, SendOneRequest request) {

    }

    @Override
    public void sendMany(String securityToken, SendManyRequest request) {

    }

    @Override
    public void sendAll(String securityToken) {

    }
}
