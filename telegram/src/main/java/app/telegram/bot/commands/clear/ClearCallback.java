package app.telegram.bot.commands.clear;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.ClearingException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ClearCallback extends AbstractBotCommand {

    public ClearCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        String answer;
        try {
            botService.clear(chatId);
            answer = "All  Contacts and Notifications were cleared";
        } catch (ClearingException e) {
            answer = "Error during clearing operation";
        }
        sendAnswer(answer);
    }
}
