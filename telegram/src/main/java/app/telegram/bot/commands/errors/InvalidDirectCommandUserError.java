package app.telegram.bot.commands.errors;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InvalidDirectCommandUserError extends AbstractBotCommand {

    public InvalidDirectCommandUserError(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        sendText("Sorry, command is not recognized, please try /help");
    }
}
