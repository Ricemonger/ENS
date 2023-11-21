package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.CallbackButton;
import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.config.BotCommandsConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationDirect extends AbstractBotCommand {

    public NotificationDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = BotCommandsConfig.NOTIFICATION_HELP_MESSAGE;
        sendAnswer(answer);
        String question = "What would you like to do?";
        CallbackButton[] buttons = new CallbackButton[4];
        buttons[0] = new CallbackButton("Add one", Callbacks.NOTIFICATION_ADD_ONE);
        buttons[1] = new CallbackButton("Add many", Callbacks.NOTIFICATION_ADD_MANY);
        buttons[2] = new CallbackButton("Delete one", Callbacks.NOTIFICATION_REMOVE_ONE);
        buttons[3] = new CallbackButton("Delete many", Callbacks.NOTIFICATION_REMOVE_MANY);
        askFromInlineKeyboard(question, 2, buttons);
    }
}
