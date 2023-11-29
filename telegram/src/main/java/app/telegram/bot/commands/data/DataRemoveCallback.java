package app.telegram.bot.commands.data;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataRemoveCallback extends AbstractBotCommand {

    public DataRemoveCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        askYesOrNoFromInlineKeyboard("Would you really like to remove all your data from bot's database?",
                Callbacks.DATA_REMOVE_FINISH, Callbacks.CANCEL);
    }
}
