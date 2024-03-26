package app.telegram.bot.commands.task.show;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TaskShowCallback extends AbstractBotCommand {

    public TaskShowCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        String text = "Your tasks:\n";
        text = text + botService.showAllTasks(chatId);
        sendText(text);
    }
}
