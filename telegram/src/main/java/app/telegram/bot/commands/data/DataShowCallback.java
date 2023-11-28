package app.telegram.bot.commands.data;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.DataShowException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataShowCallback extends AbstractBotCommand {

    public DataShowCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        try {
            String data = botService.getUserData(chatId);
            sendAnswer("Your user data:\n" + data);
        } catch (DataShowException e) {
            sendAnswer("Error occurred during data showing");
        }
    }
}
