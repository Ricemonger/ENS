package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class NotificationAddManyBodCommand extends AbstractBotCommand {

    public NotificationAddManyBodCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        List<Notification> notificationList = askManyNotifications();
        botService.addManyNotifications(chatId, notificationList);
        sendAnswer("Notifications were successfully added");
    }

    private List<Notification> askManyNotifications() {


    }
}
