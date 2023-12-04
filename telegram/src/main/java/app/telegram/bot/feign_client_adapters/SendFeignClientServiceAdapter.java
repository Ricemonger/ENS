package app.telegram.bot.feign_client_adapters;

import app.telegram.users.model.TelegramUserService;
import app.utils.services.sender.dto.SendManyRequest;
import app.utils.services.sender.dto.SendOneRequest;
import app.utils.services.sender.feign.SendFeignClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendFeignClientServiceAdapter {

    private final SendFeignClientService sendFeignClientService;

    private final TelegramUserService telegramUserService;

    public void sendOne(Long chatId, SendOneRequest request) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        sendFeignClientService.sendOne(securityToken, request);
    }

    public void sendAll(Long chatId) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        sendFeignClientService.sendAll(securityToken);
    }

    public void sendMany(Long chatId, SendManyRequest request) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        sendFeignClientService.sendMany(securityToken, request);
    }
}
