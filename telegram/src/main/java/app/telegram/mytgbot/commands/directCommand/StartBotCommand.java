package app.telegram.mytgbot.commands.directCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartBotCommand extends AbstractBotCommand {


    public StartBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = "Welcome to Emergency Notification Service Bot!";
        sendAnswer(answer);
        checkUserInDbAndAskToRegisterIfNot();
    }
}
