package app.telegram.bot.commands.settings.actionConfirmation;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ActionConfirmationCallback extends AbstractBotCommand {

    public ActionConfirmationCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        if (isUserActionConfirmFlag()) {
            String disableQuestion = "You have action confirmation ENABLED. Would you like to disable it?";
            askYesOrNoFromInlineKeyboard(disableQuestion, Callbacks.SETTINGS_ACTION_CONFIRMATION_DISABLE, Callbacks.CANCEL);
        } else {
            String enableQuestion = "Your have action confirmation DISABLED. Would you like to enable it?";
            askYesOrNoFromInlineKeyboard(enableQuestion, Callbacks.SETTINGS_ACTION_CONFIRMATION_ENABLE, Callbacks.CANCEL);
        }
    }
}
