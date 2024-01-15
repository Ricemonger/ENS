package app.telegram.bot.commands.task.deleteAll;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskDeleteAllCallback extends AbstractBotCommand {

    public TaskDeleteAllCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        String question = "Would you really like to remove ALL of tour tasks?";
        askYesOrNoFromInlineKeyboard(question, Callbacks.TASK_DELETE_ALL_FINISH, Callbacks.CANCEL);
    }
}
