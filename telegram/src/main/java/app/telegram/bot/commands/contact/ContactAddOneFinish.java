package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactAddOneFinish extends AbstractBotCommand {

    public ContactAddOneFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        new Stage4WriteNotificationAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you like to add it?", Callbacks.CONTACT_ADD_ONE_FINISH, Callbacks.CANCEL);
    }
}
