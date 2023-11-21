package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.RemovingException;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ContactRemoveManyCallback extends AbstractBotCommand {

    public ContactRemoveManyCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            List<Contact> contactList = askManyContacts();
            botService.removeManyContacts(chatId, contactList);
            sendAnswer("Contacts were successfully removed");
        } catch (RemovingException e) {
            sendAnswer("Error occurred during Contact's removing");
        }
    }

    private List<Contact> askManyContacts() {
        return null;
    }
}
