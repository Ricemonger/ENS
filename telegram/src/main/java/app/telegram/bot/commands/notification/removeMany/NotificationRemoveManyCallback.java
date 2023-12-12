package app.telegram.bot.commands.notification.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.notification.NotificationStage1AskName;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveManyCallback extends AbstractBotCommand {

    public NotificationRemoveManyCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setUserNextInputGroup(chatId, InputGroup.NOTIFICATION_REMOVE_MANY);

        new NotificationStage1AskName(bot, update, botService).execute();
    }
}
