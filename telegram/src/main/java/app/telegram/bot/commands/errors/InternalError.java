package app.telegram.bot.commands.errors;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InternalError extends AbstractBotCommand {

    public InternalError(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        sendAnswer("Internal Error Occurred");
        botService.cancelInputs(chatId);
    }
}
