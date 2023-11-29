package app.telegram.bot.commands.notification.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveOneStage2WriteNameFinishAndPrint extends AbstractBotCommand {

    public NotificationRemoveOneStage2WriteNameFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processInput(InputState.NOTIFICATION_NAME, InputState.BASE);

        String name = botService.getNotificationFromInputsMap(chatId).getName();

        sendAnswer("Your notification name is: " + name);

        botService.setNextInputGroup(chatId, InputGroup.BASE);
    }
}
