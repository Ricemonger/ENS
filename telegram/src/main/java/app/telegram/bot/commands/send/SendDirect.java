package app.telegram.bot.commands.send;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.BotCommandsConfig;
import app.telegram.bot.commands.CallbackButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendDirect extends AbstractBotCommand {

    public SendDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            String answer = BotCommandsConfig.SEND_HELP_MESSAGE;
            sendAnswer(answer);
            String question = "What would you like to do?";
            CallbackButton[] buttons = new CallbackButton[3];
            buttons[0] = new CallbackButton("Send to one", Callbacks.SEND_ONE);
            buttons[1] = new CallbackButton("Send to many", Callbacks.SEND_MANY);
            buttons[2] = new CallbackButton("Cancel operation", Callbacks.CANCEL);
            askFromInlineKeyboard(question, 2, buttons);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
