package app.telegram.bot.commands.task.delete;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskDeleteFinish extends AbstractBotCommand {

    public TaskDeleteFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        new TaskDeleteStage2WriteNameAndPrint(bot, update, botService).execute();
        askYesOrNoFromInlineKeyboard("Would you like to remove it?", Callbacks.TASK_DELETE_FINISH, Callbacks.CANCEL);
    }
}
