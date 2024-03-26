package app.telegram.bot.commands.errors;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SenderUserError extends AbstractBotCommand {

    public SenderUserError(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        sendText("Sorry, error occurred during sending operation. Invalid conjunction of Contact's Method and ID");
    }
}
