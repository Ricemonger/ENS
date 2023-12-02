package app.telegram.bot.commands.notification.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class NotificationRemoveOneChain extends AbstractBotCommand {

    public NotificationRemoveOneChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        InputState inputState = botService.getNextInputStateOrBase(chatId);
        if (Objects.requireNonNull(inputState) == InputState.NOTIFICATION_NAME) {
            new NotificationRemoveOneFinish(bot, update, botService).execute();
        } else {
            new CancelCallback(bot, update, botService).execute();
        }
    }
}
