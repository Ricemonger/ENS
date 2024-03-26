package app.telegram.bot.commands.send.sendMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendManyStage4WriteNotificationNameFinishAndPrint extends AbstractBotCommand {

    public SendManyStage4WriteNotificationNameFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processLastInput(InputState.NOTIFICATION_NAME);

        sendText("Your request is:" + botService.getSendManyRequestFromInputsMap(chatId));
    }
}
