package app.telegram.bot.commands.directCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.config.TelegramBotCommandsConfiguration;
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
