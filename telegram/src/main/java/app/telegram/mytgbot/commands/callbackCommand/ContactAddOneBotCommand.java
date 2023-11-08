package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactAddOneBotCommand extends AbstractBotCommand {

    public ContactAddOneBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        Contact contact = askOneContact();
        botService.addOneContact(chatId, contact);
        sendAnswer("Contact was successfully added");
    }

    private Contact askOneContact() {


    }
}
