package app.telegram.bot.commands.notification.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.commands.notification.Stage3WriteTextFinishAndPrint;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveManyFinish extends AbstractBotCommand {

    public NotificationRemoveManyFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        new Stage3WriteTextFinishAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you remove matching notifications?",
                Callbacks.NOTIFICATION_REMOVE_MANY_FINISH, Callbacks.CANCEL);
    }
}
