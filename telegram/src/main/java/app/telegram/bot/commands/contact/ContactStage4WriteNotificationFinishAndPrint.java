package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactStage4WriteNotificationFinishAndPrint extends AbstractBotCommand {

    public ContactStage4WriteNotificationFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processLastInput(InputState.NOTIFICATION_NAME);

        sendText("Your contact is:" + botService.getContactFromInputsMap(chatId));
    }
}
