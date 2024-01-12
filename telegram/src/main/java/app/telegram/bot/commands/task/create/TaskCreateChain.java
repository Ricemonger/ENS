package app.telegram.bot.commands.task.create;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.task.*;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskCreateChain extends AbstractBotCommand {

    public TaskCreateChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        InputState inputState = botService.getUserNextInputStateOrBase(chatId);
        switch (inputState) {
            case TASK_NAME -> new TaskStage2WriteNameAskType(bot, update, botService).execute();
            case TASK_TYPE -> new TaskStage3WriteTypeAskTime(bot, update, botService).execute();
            case TASK_TIME -> new TaskStage4WriteTimeAskMethod(bot, update, botService).execute();
            case CONTACT_METHOD -> new TaskStage5WriteMethodAskContactId(bot, update, botService).execute();
            case CONTACT_ID -> new TaskStage6WriteContactIdAskNotificationText(bot, update, botService).execute();
            case NOTIFICATION_TEXT -> new TaskCreateFinish(bot, update, botService).execute();
            default -> new CancelCallback(bot, update, botService).execute();
        }
    }
}
