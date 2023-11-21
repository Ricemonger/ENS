package app.telegram.bot.commands.start;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegisterYesCallback extends AbstractBotCommand {

    public RegisterYesCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = "Registering you in Bot's Database...";
        sendAnswer(answer);
        addUserToDb();
    }
}
