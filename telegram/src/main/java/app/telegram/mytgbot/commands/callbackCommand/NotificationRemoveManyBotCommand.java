package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class NotificationRemoveManyBotCommand extends AbstractBotCommand {

    public NotificationRemoveManyBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        List<Notification> notificationList = askManyNotifications();
        botService.removeManyNotifications(chatId, notificationList);
        sendAnswer("Notifications were successfully removed");
    }

    private List<Notification> askManyNotifications() {


    }
}
