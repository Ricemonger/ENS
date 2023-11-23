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
    public void execute() {
        processInput(InputState.CONTACT_CONTACT_ID, InputState.BASE, "Contact input is finished");
        botService.setNextInputGroup(chatId, InputGroup.BASE);

        Contact contact = botService.getContactFromInputsMap(chatId);

        String question = "Your contact is:" + contact;
        sendAnswer(question);

        askYesOrNoFromInlineKeyboard("Would you like to remove it?", Callbacks.CONTACT_REMOVE_ONE_FINISH,
                Callbacks.CANCEL);
    }
}
