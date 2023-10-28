package app.telegram.mytgbot.commands.directCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkBotCommand extends AbstractBotCommand {

    public LinkBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            //TODO LINKING
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
