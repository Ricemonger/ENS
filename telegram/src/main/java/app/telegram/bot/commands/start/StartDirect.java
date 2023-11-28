package app.telegram.bot.commands.start;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartDirect extends AbstractBotCommand {


    public StartDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        String answer = "Welcome to Emergency Notification Service Bot!";
        if (!isUserInDb(chatId)) {
            sendAnswer(answer);
            askUserToRegister();
        } else
            sendAnswer(answer + "\n You are already registered in Bot.");
    }
}
