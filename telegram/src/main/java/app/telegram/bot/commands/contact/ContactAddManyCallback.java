package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.AddingException;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ContactAddManyCallback extends AbstractBotCommand {

    public ContactAddManyCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            List<Contact> contactList = askManyContacts();
            botService.addManyContacts(chatId, contactList);
            sendAnswer("Contacts were successfully added");
        } catch (AddingException e) {
            sendAnswer("Error occurred during Contacts' adding");
        }
    }

    private List<Contact> askManyContacts() {

        return null;
    }
}
