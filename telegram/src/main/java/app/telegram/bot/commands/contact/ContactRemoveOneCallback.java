package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.RemovingException;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveOneCallback extends AbstractBotCommand {

    public ContactRemoveOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            Contact contact = askOneContact();
            botService.removeOneContact(chatId, contact);
            sendAnswer("Contact was successfully removed");
        } catch (RemovingException e) {
            sendAnswer("Error occurred during Contact's removing");
        }
    }

    private Contact askOneContact() {

        return null;
    }
}
