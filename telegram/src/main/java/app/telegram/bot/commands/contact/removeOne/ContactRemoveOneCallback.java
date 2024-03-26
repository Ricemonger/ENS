package app.telegram.bot.commands.contact.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.contact.ContactStage1AskMethod;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveOneCallback extends AbstractBotCommand {

    public ContactRemoveOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setUserNextInputGroup(chatId, InputGroup.CONTACT_REMOVE_ONE);

        new ContactStage1AskMethod(bot, update, botService).execute();
    }
}
