package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
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
            username = "";
            sendAnswer("Please enter your ENS account's password:");
            password = "";
            botService.link(chatId, username, password);
            //TODO LINKING
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
