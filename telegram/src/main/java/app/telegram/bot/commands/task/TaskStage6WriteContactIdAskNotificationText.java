package app.telegram.bot.commands.task;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskStage6WriteContactIdAskNotificationText extends AbstractBotCommand {

    public TaskStage6WriteContactIdAskNotificationText(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.CONTACT_METHOD, InputState.NOTIFICATION_TEXT, "Please input task's contact's " +
                "notification text");
    }
}
