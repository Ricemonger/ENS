package app.telegram.bot.commands.task;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskStage3WriteTypeAskTime extends AbstractBotCommand {

    public TaskStage3WriteTypeAskTime(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        String text = String.format("Please input task's time and date as example:23:59:59 13-01-2024, in format<%s>:"
                , BotService.TIME_AND_DATE_FORMAT);

        processMiddleInput(InputState.TASK_TYPE, InputState.TASK_TIME, text);
    }
}
