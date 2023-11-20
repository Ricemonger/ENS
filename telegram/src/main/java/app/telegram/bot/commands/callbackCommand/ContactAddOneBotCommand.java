package app.telegram.bot.commands.callbackCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
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

        return null;
    }
}
