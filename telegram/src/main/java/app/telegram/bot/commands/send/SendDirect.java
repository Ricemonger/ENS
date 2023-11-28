package app.telegram.bot.commands.send;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.CallbackButton;
import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.config.BotCommandsConfig;
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
            buttons[0] = new CallbackButton("Send to one contact", Callbacks.SEND_ONE);
            buttons[1] = new CallbackButton("Send to many by filter", Callbacks.SEND_MANY);
            buttons[2] = new CallbackButton("Cancel operation", Callbacks.CANCEL);
            askFromInlineKeyboard(question, 2, buttons);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
