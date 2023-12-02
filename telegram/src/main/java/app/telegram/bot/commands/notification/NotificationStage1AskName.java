package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationStage1AskName extends AbstractBotCommand {

    public NotificationStage1AskName(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processFirstInput(chatId, InputState.NOTIFICATION_NAME, "Please input notification template's name:");
    }
}
