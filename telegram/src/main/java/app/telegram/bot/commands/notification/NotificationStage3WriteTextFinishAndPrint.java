package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationStage3WriteTextFinishAndPrint extends AbstractBotCommand {

    public NotificationStage3WriteTextFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processLastInput(InputState.NOTIFICATION_TEXT);

        sendAnswer("Your notification is:" + botService.getNotificationFromInputsMap(chatId));
    }
}
