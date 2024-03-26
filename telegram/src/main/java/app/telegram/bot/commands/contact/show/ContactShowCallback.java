package app.telegram.bot.commands.contact.show;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactShowCallback extends AbstractBotCommand {

    public ContactShowCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        String text = "Your contacts:\n";
        text = text + botService.showAllContacts(chatId);
        sendText(text);
    }
}
