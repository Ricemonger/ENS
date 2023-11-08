package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegisterYesBotCommand extends AbstractBotCommand {

    public RegisterYesBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = "Registering you in Bot's Database...";
        sendAnswer(answer);
        addUserToDb();
    }
}
