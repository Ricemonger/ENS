package app.telegram.bot.commands.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.LinkingException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkCallback extends AbstractBotCommand {

    public LinkCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            try {
                String username;
                String password;
                sendAnswer("Linking your telegram account to your ENS account...");
                sendAnswer("Please enter your ENS account's username:");

                sendAnswer("Please enter your ENS account's password:");

            } catch (LinkingException e) {
                sendAnswer("Error occurred during linking operation");
            }
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
