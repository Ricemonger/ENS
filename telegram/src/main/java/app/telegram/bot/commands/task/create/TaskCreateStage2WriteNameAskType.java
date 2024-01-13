package app.telegram.bot.commands.task.create;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskCreateStage2WriteNameAskType extends AbstractBotCommand {

    public TaskCreateStage2WriteNameAskType(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.TASK_NAME, InputState.TASK_TYPE, "Please input task's type[ONE,MANY,ALL]:");
    }
}
