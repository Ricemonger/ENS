package app.telegram.bot.commands.contact.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.contact.ContactStage1AskMethod;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveManyCallback extends AbstractBotCommand {

    public ContactRemoveManyCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setNextInputGroup(chatId, InputGroup.CONTACT_REMOVE_MANY);

        new ContactStage1AskMethod(bot, update, botService).execute();
    }
}
