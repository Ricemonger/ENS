package app.telegram.bot.commands.send.sendOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendOneStage4WriteNotificationTextFinishRequestAndPrint extends AbstractBotCommand {

    public SendOneStage4WriteNotificationTextFinishRequestAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processInput(InputState.NOTIFICATION_TEXT, InputState.BASE);

        SendOneRequest request = botService.getSendOneRequestFromInputsMap(chatId);

        sendAnswer("Your request is:" + request);

        botService.setNextInputGroup(chatId, InputGroup.BASE);
    }
}
