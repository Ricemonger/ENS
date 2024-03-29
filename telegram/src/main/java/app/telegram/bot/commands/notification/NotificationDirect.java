package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.BotCommandsConfig;
import app.telegram.bot.commands.CallbackButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationDirect extends AbstractBotCommand {

    public NotificationDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            sendText(BotCommandsConfig.NOTIFICATION_HELP_MESSAGE);
            String question = "What would you like to do?";
            CallbackButton[] buttons = new CallbackButton[3];
            buttons[0] = new CallbackButton("Add one", Callbacks.NOTIFICATION_ADD);
            buttons[1] = new CallbackButton("Delete one", Callbacks.NOTIFICATION_REMOVE_ONE);
            buttons[2] = new CallbackButton("Delete many", Callbacks.NOTIFICATION_REMOVE_MANY);
            askFromInlineKeyboard(question, 1, buttons);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
