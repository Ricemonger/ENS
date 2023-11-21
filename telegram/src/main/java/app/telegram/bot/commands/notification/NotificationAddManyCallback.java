package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.AddingException;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class NotificationAddManyCallback extends AbstractBotCommand {

    public NotificationAddManyCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            List<Notification> notificationList = askManyNotifications();
            botService.addManyNotifications(chatId, notificationList);
            sendAnswer("Notifications were successfully added");
        } catch (AddingException e) {
            sendAnswer("Error occurred during adding notifications");
        }
    }

    private List<Notification> askManyNotifications() {
        return null;
    }
}
