package app.telegram.bot.commands.callbackCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkAddYesBotCommand extends AbstractBotCommand {

    public LinkAddYesBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            String username;
            String password;
            sendAnswer("Linking your telegram account to your ENS account...");
            sendAnswer("Please enter your ENS account's username:");
            username = null;
            sendAnswer("Please enter your ENS account's password:");
            password = null;
            botService.link(chatId, username, password);
            //TODO LINKING
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
