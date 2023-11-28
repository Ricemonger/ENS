package app.telegram.bot.commands.invalid;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InvalidDirect extends AbstractBotCommand {

    public InvalidDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        String answer = "Sorry, command is not recognized";
        sendAnswer(answer);
    }
}
