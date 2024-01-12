package app.telegram.bot.commands.data;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.BotCommandsConfig;
import app.telegram.bot.commands.CallbackButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataDirect extends AbstractBotCommand {

    public DataDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            sendText(BotCommandsConfig.DATA_HELP_MESSAGE);
            String question = "What would you like to do?";
            CallbackButton[] buttons = new CallbackButton[2];
            buttons[0] = new CallbackButton("Show data", Callbacks.DATA_SHOW);
            buttons[1] = new CallbackButton("Remove data", Callbacks.DATA_REMOVE);
            askFromInlineKeyboard(question, 2, buttons);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
