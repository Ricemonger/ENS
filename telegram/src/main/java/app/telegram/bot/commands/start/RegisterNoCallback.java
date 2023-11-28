package app.telegram.bot.commands.start;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegisterNoCallback extends AbstractBotCommand {

    public RegisterNoCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        String answer = "You chose not to register in bot's database.\n Most of the functionality will not be " +
                "available for you, but you can register at any time";
        sendAnswer(answer);
    }
}
