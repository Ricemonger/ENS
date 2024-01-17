package app.telegram.bot.commands.task;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskStage1AskName extends AbstractBotCommand {

    public TaskStage1AskName(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processFirstInput(chatId, InputState.TASK_NAME, "Please input task's name:");
    }
}
