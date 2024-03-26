package app.telegram.bot.commands.contact.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveOneFinish extends AbstractBotCommand {

    public ContactRemoveOneFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        new ContactRemoveOneStage3WriteContactIdFinishAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you like to remove contact", Callbacks.CONTACT_REMOVE_ONE_FINISH,
                Callbacks.CANCEL);
    }
}
