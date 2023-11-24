package app.telegram.bot.commands.contact.add;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.contact.Stage1AskMethod;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactAddCallback extends AbstractBotCommand {

    public ContactAddCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            botService.setNextInputGroup(chatId, InputGroup.CONTACT_ADD_ONE);

            new Stage1AskMethod(bot, update, botService).execute();
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
