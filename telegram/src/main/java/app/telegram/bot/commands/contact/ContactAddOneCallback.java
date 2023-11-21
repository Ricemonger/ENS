package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.AddingException;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactAddOneCallback extends AbstractBotCommand {

    public ContactAddOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            Contact contact = askOneContact();
            botService.addOneContact(chatId, contact);
            sendAnswer("Contact was successfully added");
        } catch (AddingException e) {
            sendAnswer("Error occurred during Contact's adding");
        }
    }

    private Contact askOneContact() {

        return null;
    }
}
