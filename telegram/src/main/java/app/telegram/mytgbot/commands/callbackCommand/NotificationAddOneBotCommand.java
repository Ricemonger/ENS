package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import app.telegram.service.notification.Notification;
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
        botService.addOneNotification(notification);
        sendAnswer("Notification was successfully added");
    }

    private Notification askOneNotification() {

    }

}
