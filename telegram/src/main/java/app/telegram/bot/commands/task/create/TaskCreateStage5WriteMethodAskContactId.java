package app.telegram.bot.commands.task.create;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskCreateStage5WriteMethodAskContactId extends AbstractBotCommand {

    public TaskCreateStage5WriteMethodAskContactId(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.CONTACT_METHOD, InputState.CONTACT_ID, "Please input task's contact's id" +
                "(phone number or email, depending on method):");
    }
}
