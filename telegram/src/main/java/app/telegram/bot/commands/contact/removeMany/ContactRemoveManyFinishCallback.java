package app.telegram.bot.commands.contact.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveManyFinishCallback extends AbstractBotCommand {

    public ContactRemoveManyFinishCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        botService.removeManyContacts(chatId);
        sendAnswer("Your Contacts was Deleted!");
    }
}
