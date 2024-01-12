package app.telegram.bot.commands.task;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskStage5WriteMethodAskContactId extends AbstractBotCommand {

    public TaskStage5WriteMethodAskContactId(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.CONTACT_METHOD, InputState.CONTACT_ID, "Please input task's contact's method:");
    }
}
