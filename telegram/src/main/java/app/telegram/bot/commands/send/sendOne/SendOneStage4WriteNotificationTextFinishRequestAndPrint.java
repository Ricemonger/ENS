package app.telegram.bot.commands.send.sendOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendOneStage4WriteNotificationTextFinishRequestAndPrint extends AbstractBotCommand {

    public SendOneStage4WriteNotificationTextFinishRequestAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processLastInput(InputState.NOTIFICATION_TEXT);

        sendAnswer("Your request is:" + botService.getSendOneRequestFromInputsMap(chatId));
    }
}
