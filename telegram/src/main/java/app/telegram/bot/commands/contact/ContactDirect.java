package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.BotCommandsConfig;
import app.telegram.bot.commands.CallbackButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactDirect extends AbstractBotCommand {

    public ContactDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            sendText(BotCommandsConfig.CONTACT_HELP_MESSAGE);
            String question = "What would you like to do?";
            CallbackButton[] buttons = new CallbackButton[4];
            buttons[0] = new CallbackButton("Show All", Callbacks.CONTACT_SHOW);
            buttons[1] = new CallbackButton("Add one", Callbacks.CONTACT_ADD);
            buttons[2] = new CallbackButton("Delete one", Callbacks.CONTACT_REMOVE_ONE);
            buttons[3] = new CallbackButton("Delete many", Callbacks.CONTACT_REMOVE_MANY);
            askFromInlineKeyboard(question, 1, buttons);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
