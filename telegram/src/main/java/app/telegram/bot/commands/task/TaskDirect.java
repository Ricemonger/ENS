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
            CallbackButton createTask = new CallbackButton("Create Task", Callbacks.TASK_CREATE);
            CallbackButton removeTask = new CallbackButton("Remove Task", Callbacks.TASK_DELETE);
            CallbackButton cancel = new CallbackButton("Cancel", Callbacks.CANCEL);
            askFromInlineKeyboard(question, 1, createTask, removeTask, cancel);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
