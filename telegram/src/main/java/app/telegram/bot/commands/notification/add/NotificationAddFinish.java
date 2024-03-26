package app.telegram.bot.commands.notification.add;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.notification.NotificationStage3WriteTextFinishAndPrint;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationAddFinish extends AbstractBotCommand {

    public NotificationAddFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        new NotificationStage3WriteTextFinishAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you like to add it?", Callbacks.NOTIFICATION_ADD_FINISH, Callbacks.CANCEL);
    }
}
