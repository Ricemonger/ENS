package app.telegram.bot.commands.directCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ClearBotCommand extends AbstractBotCommand {

    public ClearBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            String answer = "Do you really want to clear all your Contacts and Notifications?";
            sendAnswer(answer);
            askYesOrNoFromInlineKeyboard("CLEAR_YES", "CLEAR_NO");
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
