package app.telegram.bot.commands.send.sendOne;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendOneFinish extends AbstractBotCommand {

    public SendOneFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        new SendOneStage4WriteNotificationTextFinishRequestAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you like to send notification to defined contact?", Callbacks.SEND_ONE_FINISH, Callbacks.CANCEL);
    }
}
