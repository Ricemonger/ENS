package app.telegram.bot.commands.task.deleteAll;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskDeleteAllFinishCallback extends AbstractBotCommand {

    public TaskDeleteAllFinishCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        botService.deleteAllTasks(chatId);
        sendText("All of your tasks were successfully deleted!");
    }
}
