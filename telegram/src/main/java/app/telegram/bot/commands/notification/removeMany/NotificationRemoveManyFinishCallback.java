package app.telegram.bot.commands.notification.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveManyFinishCallback extends AbstractBotCommand {

    public NotificationRemoveManyFinishCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.removeManyNotifications(chatId);
        sendText("Your Notifications were removed!");
    }
}
