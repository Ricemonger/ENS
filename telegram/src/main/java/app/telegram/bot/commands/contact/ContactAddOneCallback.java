package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactAddOneCallback extends AbstractBotCommand {

    public ContactAddOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        botService.setNextInputGroup(chatId, InputGroup.CONTACT_ADD_ONE);

        new Stage1AskMethod(bot, update, botService).execute();
    }
}
