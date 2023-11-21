package app.telegram.bot.commands.data;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.DataRemoveException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataRemoveCallback extends AbstractBotCommand {

    public DataRemoveCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            botService.removeAllData(chatId);
            sendAnswer("All your data was removed");
        } catch (DataRemoveException e) {
            sendAnswer("Error occurred during data removing");
        }
    }
}
