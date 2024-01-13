package app.telegram.bot.commands.task.create;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskCreateStage4WriteTimeAskMethod extends AbstractBotCommand {

    public TaskCreateStage4WriteTimeAskMethod(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.TASK_TIME, InputState.CONTACT_METHOD, "Please input task's contact's method[SMS," +
                "VIBER,EMAIL,TELEGRAM]:");
    }
}
