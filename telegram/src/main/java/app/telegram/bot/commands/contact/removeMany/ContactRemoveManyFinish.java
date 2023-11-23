package app.telegram.bot.commands.contact.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.commands.contact.Stage4WriteNotificationAndPrint;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveManyFinish extends AbstractBotCommand {

    public ContactRemoveManyFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        new Stage4WriteNotificationAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you like to remove matching contacts?",
                Callbacks.CONTACT_REMOVE_MANY_FINISH,
                Callbacks.CANCEL);
    }
}
