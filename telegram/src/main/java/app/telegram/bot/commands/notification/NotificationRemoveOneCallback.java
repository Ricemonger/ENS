package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.RemovingException;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveOneCallback extends AbstractBotCommand {

    public NotificationRemoveOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            Notification notification = askOneNotification();
            botService.removeOneNotifications(chatId, notification);
            sendAnswer("Notification was successfully removed");
        } catch (RemovingException e) {
            sendAnswer("Error occurred during removing notifications");
        }
    }

    private Notification askOneNotification() {
        return null;
    }
}
