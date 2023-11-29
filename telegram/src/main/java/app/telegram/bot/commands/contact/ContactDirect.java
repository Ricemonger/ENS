package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.CallbackButton;
import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.config.BotCommandsConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactDirect extends AbstractBotCommand {

    public ContactDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            sendAnswer(BotCommandsConfig.CONTACT_HELP_MESSAGE);
            String question = "What would you like to do?";
            CallbackButton[] buttons = new CallbackButton[3];
            buttons[0] = new CallbackButton("Add one", Callbacks.CONTACT_ADD);
            buttons[1] = new CallbackButton("Delete one", Callbacks.CONTACT_REMOVE_ONE);
            buttons[2] = new CallbackButton("Delete many", Callbacks.CONTACT_REMOVE_MANY);
            askFromInlineKeyboard(question, 1, buttons);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
