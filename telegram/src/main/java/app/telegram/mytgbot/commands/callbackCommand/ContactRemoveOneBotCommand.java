package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import app.utils.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveOneBotCommand extends AbstractBotCommand {

    public ContactRemoveOneBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        //TODO ASK CONTACTS
        Contact contact = askOneContact();
        botService.removeOneContact(chatId, contact);
        sendAnswer("Contact was successfully removed");
    }

    private Contact askOneContact() {


    }
}
