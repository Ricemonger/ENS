package app.telegram.bot.commands.callbackCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.utils.feign_clients.contact.Contact;
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
        botService.removeManyContacts(chatId, contactList);
        sendAnswer("Contacts were successfully removed");
    }

    private List<Contact> askManyContacts() {
        return null;
    }
}
