package app.telegram.bot.commands.clear;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ClearDirect extends AbstractBotCommand {

    public ClearDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            String answer = "Do you really want to clear all your Contacts and Notifications?";
            sendAnswer(answer);
            askYesOrNoFromInlineKeyboard(answer, Callbacks.CLEAR, Callbacks.CANCEL);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
