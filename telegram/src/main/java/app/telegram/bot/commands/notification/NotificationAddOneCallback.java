package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.AddingException;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationAddOneCallback extends AbstractBotCommand {

    public NotificationAddOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            Notification notification = askOneNotification();
            botService.addOneNotification(chatId, notification);
            sendAnswer("Notification was successfully added");
        } catch (AddingException e) {
            sendAnswer("Error occurred during adding notifications");
        }
    }

    private Notification askOneNotification() {
        return null;
    }

}
