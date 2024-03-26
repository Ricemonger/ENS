package app.telegram.bot.commands.task.delete;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskDeleteStage2WriteNameAndPrint extends AbstractBotCommand {

    public TaskDeleteStage2WriteNameAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processLastInput(InputState.TASK_NAME);

        String text = "Your task is: " + botService.getTaskKeyFromInputMap(chatId).toString();

        sendText(text);
    }
}
