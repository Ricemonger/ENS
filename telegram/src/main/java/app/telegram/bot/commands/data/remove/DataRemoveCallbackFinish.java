package app.telegram.bot.commands.data.remove;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.DataRemoveException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataRemoveCallbackFinish extends AbstractBotCommand {

    public DataRemoveCallbackFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        try {
            botService.removeAllData(chatId);
            sendAnswer("All your data was removed");
        } catch (DataRemoveException e) {
            sendAnswer("Error occurred during data removing");
        }
    }
}
