package app.telegram.bot.commands.clear;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ClearDirect extends AbstractBotCommand {

    public ClearDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            askYesOrNoFromInlineKeyboard("Do you really want to clear all your Contacts and Notifications?", Callbacks.CLEAR, Callbacks.CANCEL);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
