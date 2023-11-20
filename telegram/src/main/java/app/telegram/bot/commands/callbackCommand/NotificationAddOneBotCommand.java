package app.telegram.bot.commands.callbackCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationAddOneBotCommand extends AbstractBotCommand {

    public NotificationAddOneBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        Notification notification = askOneNotification();
        botService.addOneNotification(chatId, notification);
        sendAnswer("Notification was successfully added");
    }

    private Notification askOneNotification() {
        return null;
    }

}
