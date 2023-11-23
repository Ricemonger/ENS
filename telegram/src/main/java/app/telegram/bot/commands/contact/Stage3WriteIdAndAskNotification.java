package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage3WriteIdAndAskNotification extends AbstractBotCommand {

    public Stage3WriteIdAndAskNotification(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        processInput(InputState.CONTACT_CONTACT_ID, InputState.CONTACT_NOTIFICATION_NAME, "Please input your " +
                "Notification Template's name");
    }
}