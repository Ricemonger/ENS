package app.telegram.bot.feign_client_wrappers;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.sender.SendFeignClientService;
import app.utils.feign_clients.sender.dto.SendManyRequest;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendFeignClientServiceAdapter {

    private final SendFeignClientService sendFeignClientService;

    private final TelegramUserService telegramUserService;

    public void sendAll(Long chatId) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        sendFeignClientService.sendAll(securityToken);
    }

    public void sendOne(Long chatId, SendOneRequest request) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        sendFeignClientService.sendOne(securityToken, request);
    }

    public void sendMany(Long chatId, SendManyRequest request) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        sendFeignClientService.sendMany(securityToken, request);
    }
}
