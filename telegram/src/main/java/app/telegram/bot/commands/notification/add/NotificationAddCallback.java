package app.telegram.bot.commands.notification.add;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.notification.NotificationStage1AskName;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationAddCallback extends AbstractBotCommand {

    public NotificationAddCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setUserNextInputGroup(chatId, InputGroup.NOTIFICATION_ADD_ONE);

        new NotificationStage1AskName(bot, update, botService).execute();
    }
}

