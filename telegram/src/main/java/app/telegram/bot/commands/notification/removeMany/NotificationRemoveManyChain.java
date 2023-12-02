package app.telegram.bot.commands.notification.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.notification.NotificationStage2WriteNameAskText;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveManyChain extends AbstractBotCommand {

    public NotificationRemoveManyChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        InputState inputState = botService.getNextInputStateOrBase(chatId);
        switch (inputState) {
            case NOTIFICATION_NAME -> new NotificationStage2WriteNameAskText(bot, update, botService).execute();
            case NOTIFICATION_TEXT -> new NotificationRemoveManyFinish(bot, update, botService).execute();
            default -> new CancelCallback(bot, update, botService).execute();
        }
    }
}
