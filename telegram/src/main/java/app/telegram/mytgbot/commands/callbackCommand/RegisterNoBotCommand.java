package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegisterNoBotCommand extends AbstractBotCommand {

    public RegisterNoBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = "You chose not to register in bot's database.\n Most of the functionality will not be " +
                "available for you, but you can register at any time";
        sendAnswer(answer);
    }
}
