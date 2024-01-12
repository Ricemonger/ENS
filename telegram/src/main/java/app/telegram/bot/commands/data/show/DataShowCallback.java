package app.telegram.bot.commands.data.show;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataShowCallback extends AbstractBotCommand {

    public DataShowCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        String data = botService.getUserData(chatId);
        sendText("Your user data:\n" + data);
    }
}
