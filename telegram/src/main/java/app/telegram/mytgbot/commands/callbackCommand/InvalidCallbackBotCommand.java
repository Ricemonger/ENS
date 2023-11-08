package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InvalidCallbackBotCommand extends AbstractBotCommand {

    public InvalidCallbackBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = "Sorry, query is not recognized";
        sendAnswer(answer);
    }
}
