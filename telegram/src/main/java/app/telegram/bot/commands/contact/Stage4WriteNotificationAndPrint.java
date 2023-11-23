package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.feign_clients.contact.Contact;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage4WriteNotificationAndPrint extends AbstractBotCommand {

    public Stage4WriteNotificationAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        processInput(InputState.CONTACT_NOTIFICATION_NAME, InputState.BASE, "Contact input is finished");
        botService.setNextInputGroup(chatId, InputGroup.BASE);

        Contact contact = botService.getContactFromInputsMap(chatId);

        String question = "Your contact is:" + contact;
        sendAnswer(question);
    }
}
