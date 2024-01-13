package app.telegram.bot.commands.task.delete;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class TaskDeleteChain extends AbstractBotCommand {

    public TaskDeleteChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        InputState inputState = botService.getUserNextInputStateOrBase(chatId);
        if (Objects.requireNonNull(inputState) == InputState.TASK_NAME) {
            new TaskDeleteFinish(bot, update, botService).execute();
        } else {
            new CancelCallback(bot, update, botService).execute();
        }
    }
}
