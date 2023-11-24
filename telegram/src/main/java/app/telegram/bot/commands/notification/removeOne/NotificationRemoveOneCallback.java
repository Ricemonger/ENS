package app.telegram.bot.commands.notification.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.notification.Stage1AskName;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotificationRemoveOneCallback extends AbstractBotCommand {

    public NotificationRemoveOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            botService.setNextInputGroup(chatId, InputGroup.NOTIFICATION_REMOVE_ONE);

            new Stage1AskName(bot, update, botService).execute();
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
