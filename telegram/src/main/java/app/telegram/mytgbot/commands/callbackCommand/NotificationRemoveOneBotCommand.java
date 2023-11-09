package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveOneBotCommand extends AbstractBotCommand {

    public NotificationRemoveOneBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        Notification notification = askOneNotification();
        botService.removeOneNotifications(chatId, notification);
        sendAnswer("Notification was successfully removed");
    }

    private Notification askOneNotification() {


    }
}
