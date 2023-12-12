package app.telegram.bot.commands.settings.actionConfirmation;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ActionConfirmationDisableCallback extends AbstractBotCommand {

    public ActionConfirmationDisableCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        setUserActionConfirmFlag(false);
        sendAnswer("Action confirmation was disabled.");
    }
}
