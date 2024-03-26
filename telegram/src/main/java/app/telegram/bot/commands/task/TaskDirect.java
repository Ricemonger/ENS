package app.telegram.bot.commands.task;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.CallbackButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskDirect extends AbstractBotCommand {

    public TaskDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        MyFunctionalInterface function = () -> {
            String question = "What would you like to do?";
            CallbackButton showTasks = new CallbackButton("Show All Tasks", Callbacks.TASK_SHOW);
            CallbackButton createTask = new CallbackButton("Create Task", Callbacks.TASK_CREATE);
            CallbackButton deleteTask = new CallbackButton("Delete Task", Callbacks.TASK_DELETE);
            CallbackButton deleteAllTasks = new CallbackButton("Delete ALL Tasks", Callbacks.TASK_DELETE_ALL);
            CallbackButton cancel = new CallbackButton("Cancel", Callbacks.CANCEL);
            askFromInlineKeyboard(question, 1, showTasks, createTask, deleteTask, deleteAllTasks, cancel);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
