package app.telegram.bot.commands.settings.actionConfirmation;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ActionConfirmationEnableCallback extends AbstractBotCommand {

    public ActionConfirmationEnableCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        setUserActionConfirmFlag(true);
        sendText("Action confirmation was enabled.");
    }
}
