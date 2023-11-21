package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.RemovingException;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class NotificationRemoveManyCallback extends AbstractBotCommand {

    public NotificationRemoveManyCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            List<Notification> notificationList = askManyNotifications();
            botService.removeManyNotifications(chatId, notificationList);
            sendAnswer("Notifications were successfully removed");
        } catch (RemovingException e) {
            sendAnswer("Error occurred during removing notifications");
        }
    }

    private List<Notification> askManyNotifications() {
        return null;
    }
}
