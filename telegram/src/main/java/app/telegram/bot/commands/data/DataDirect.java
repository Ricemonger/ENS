package app.telegram.bot.commands.data;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.CallbackButton;
import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.config.BotCommandsConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataDirect extends AbstractBotCommand {

    public DataDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = BotCommandsConfig.DATA_HELP_MESSAGE;
        sendAnswer(answer);
        String question = "What would you like to do?";
        CallbackButton[] buttons = new CallbackButton[2];
        buttons[0] = new CallbackButton("Show data", Callbacks.DATA_SHOW);
        buttons[1] = new CallbackButton("Remove data", Callbacks.DATA_REMOVE);
        askFromInlineKeyboard(question, 2, buttons);
    }
}
