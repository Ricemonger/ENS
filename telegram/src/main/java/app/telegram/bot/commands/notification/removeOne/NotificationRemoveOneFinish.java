package app.telegram.bot.commands.notification.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveOneFinish extends AbstractBotCommand {

    public NotificationRemoveOneFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        new NotificationRemoveOneStage2WriteNameFinishAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you remove notification?", Callbacks.NOTIFICATION_REMOVE_ONE_FINISH, Callbacks.CANCEL);
    }
}
