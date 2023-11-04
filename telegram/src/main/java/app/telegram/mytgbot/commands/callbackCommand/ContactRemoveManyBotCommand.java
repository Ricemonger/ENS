package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import app.telegram.service.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ContactRemoveManyBotCommand extends AbstractBotCommand {

    public ContactRemoveManyBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        List<Contact> contactList = askManyContacts();
        botService.removeManyContacts(contactList);
        sendAnswer("Contacts were successfully removed");
    }

    private List<Contact> askManyContacts() {

    }
}
