package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ContactAddManyBotCommand extends AbstractBotCommand {

    public ContactAddManyBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        List<Contact> contactList = askManyContacts();
        botService.addManyContacts(chatId, contactList);
        sendAnswer("Contacts were successfully added");
    }

    private List<Contact> askManyContacts() {


    }
}
