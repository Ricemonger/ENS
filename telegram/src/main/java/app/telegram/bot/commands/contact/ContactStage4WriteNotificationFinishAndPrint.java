package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactStage4WriteNotificationFinishAndPrint extends AbstractBotCommand {

    public ContactStage4WriteNotificationFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processInput(InputState.NOTIFICATION_NAME, InputState.BASE);

        Contact contact = botService.getContactFromInputsMap(chatId);

        sendAnswer("Your contact is:" + contact);

        botService.setNextInputGroup(chatId, InputGroup.BASE);
    }
}
