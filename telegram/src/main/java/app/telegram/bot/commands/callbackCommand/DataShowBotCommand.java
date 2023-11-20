package app.telegram.bot.commands.callbackCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataShowBotCommand extends AbstractBotCommand {

    public DataShowBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String data = botService.getUserData(chatId);
        sendAnswer("Your user data:\n" + data);
    }
}
