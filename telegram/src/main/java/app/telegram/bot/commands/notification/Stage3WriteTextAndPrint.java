package app.telegram.bot.commands.notification;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.feign_clients.notification.Notification;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage3WriteTextAndPrint extends AbstractBotCommand {

    public Stage3WriteTextAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        Notification notification = botService.getNotificationFromInputsMap(chatId);

        processInput(InputState.NOTIFICATION_TEXT, InputState.BASE, "Your notification is:" + notification);

        botService.setNextInputGroup(chatId, InputGroup.BASE);
    }
}
