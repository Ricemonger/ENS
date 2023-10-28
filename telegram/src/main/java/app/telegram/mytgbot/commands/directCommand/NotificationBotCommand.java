package app.telegram.mytgbot.commands.directCommand;

import app.telegram.mytgbot.TelegramBotCommandsConfiguration;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationBotCommand extends AbstractBotCommand {

    public NotificationBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = TelegramBotCommandsConfiguration.NOTIFICATION_HELP_MESSAGE;
        sendAnswer(answer);
        //TODO KEYBOARD NOTIFICATION
    }
}
