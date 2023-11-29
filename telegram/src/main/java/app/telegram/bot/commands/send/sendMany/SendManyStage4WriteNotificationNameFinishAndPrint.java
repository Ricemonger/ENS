package app.telegram.bot.commands.send.sendMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.feign_clients.sender.dto.SendManyRequest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendManyStage4WriteNotificationNameFinishAndPrint extends AbstractBotCommand {

    public SendManyStage4WriteNotificationNameFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processInput(InputState.NOTIFICATION_NAME, InputState.BASE);

        SendManyRequest request = botService.getSendManyRequestFromInputsMap(chatId);

        sendAnswer("Your request is:" + request);

        botService.setNextInputGroup(chatId, InputGroup.BASE);
    }
}
