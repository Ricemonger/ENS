package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactAddOneFinishCallback extends AbstractBotCommand {

    public ContactAddOneFinishCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        botService.addContactFromInputMap(chatId);
        sendAnswer("Your Contact is Saved!");
    }
}
