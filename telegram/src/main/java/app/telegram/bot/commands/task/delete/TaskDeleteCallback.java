package app.telegram.bot.commands.task.delete;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.task.TaskStage1AskName;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskDeleteCallback extends AbstractBotCommand {

    public TaskDeleteCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        botService.setUserNextInputGroup(chatId, InputGroup.TASK_DELETE);

        new TaskStage1AskName(bot, update, botService).execute();
    }
}
