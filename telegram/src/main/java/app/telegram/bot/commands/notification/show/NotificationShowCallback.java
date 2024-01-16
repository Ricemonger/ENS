package app.telegram.bot.commands.notification.show;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationShowCallback extends AbstractBotCommand {

    public NotificationShowCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        String text = "Your notifications:\n";
        text = text + botService.showAllNotifications(chatId);
        sendText(text);
    }
}
