package app.telegram.bot.commands.notification.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveOneFinish extends AbstractBotCommand {

    public NotificationRemoveOneFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String name = botService.getNotificationFromInputsMap(chatId).getName();
        processInput(InputState.NOTIFICATION_NAME, InputState.BASE, "Your notification name is: " + name);

        botService.setNextInputGroup(chatId, InputGroup.BASE);

        askYesOrNoFromInlineKeyboard("Would you remove notification?", Callbacks.NOTIFICATION_REMOVE_ONE_FINISH, Callbacks.CANCEL);
    }
}
