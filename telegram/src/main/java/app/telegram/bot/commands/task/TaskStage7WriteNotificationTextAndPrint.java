package app.telegram.bot.commands.task;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskStage7WriteNotificationTextAndPrint extends AbstractBotCommand {

    public TaskStage7WriteNotificationTextAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processLastInput(InputState.NOTIFICATION_TEXT);

        String text = botService.getTaskFromInputMap(chatId).toString();

        sendText(text);
    }
}
