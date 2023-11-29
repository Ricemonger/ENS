package app.telegram.bot.commands.contact.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveOneFinish extends AbstractBotCommand {

    public ContactRemoveOneFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processInput(InputState.CONTACT_ID, InputState.BASE);

        Contact contact = botService.getContactFromInputsMap(chatId);

        sendAnswer("Your contact is:" + contact);

        botService.setNextInputGroup(chatId, InputGroup.BASE);

        askYesOrNoFromInlineKeyboard("Would you like to remove contact", Callbacks.CONTACT_REMOVE_ONE_FINISH,
                Callbacks.CANCEL);
    }
}
