package app.telegram.bot.commands.task.create;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskCreateStage7WriteNotificationTextAndPrint extends AbstractBotCommand {

    public TaskCreateStage7WriteNotificationTextAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processLastInput(InputState.NOTIFICATION_TEXT);

        String text = "Your task is: " + botService.getTaskFromInputMap(chatId).toString();

        sendText(text);
    }
}
