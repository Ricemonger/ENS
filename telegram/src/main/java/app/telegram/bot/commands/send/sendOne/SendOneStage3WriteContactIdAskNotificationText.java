package app.telegram.bot.commands.send.sendOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendOneStage3WriteContactIdAskNotificationText extends AbstractBotCommand {

    public SendOneStage3WriteContactIdAskNotificationText(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processInput(InputState.CONTACT_ID, InputState.NOTIFICATION_TEXT, "Please input notification text:");
    }
}
