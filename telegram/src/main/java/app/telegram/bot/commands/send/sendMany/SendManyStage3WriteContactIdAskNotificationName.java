package app.telegram.bot.commands.send.sendMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendManyStage3WriteContactIdAskNotificationName extends AbstractBotCommand {

    public SendManyStage3WriteContactIdAskNotificationName(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.CONTACT_ID, InputState.NOTIFICATION_NAME, "Please input notification name:");
    }
}
