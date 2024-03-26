package app.telegram.bot.commands.task.create;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskCreateFinish extends AbstractBotCommand {

    public TaskCreateFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        new TaskCreateStage7WriteNotificationTextAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you like to add it?", Callbacks.TASK_CREATE_FINISH, Callbacks.CANCEL);
    }
}
