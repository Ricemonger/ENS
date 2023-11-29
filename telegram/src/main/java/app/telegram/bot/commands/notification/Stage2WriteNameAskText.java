package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage2WriteNameAskText extends AbstractBotCommand {

    public Stage2WriteNameAskText(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processInput(InputState.NOTIFICATION_NAME, InputState.NOTIFICATION_TEXT, "Please input notification template's " +
                "text:");
    }
}
