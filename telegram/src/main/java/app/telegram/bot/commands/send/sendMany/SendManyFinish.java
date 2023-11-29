package app.telegram.bot.commands.send.sendMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendManyFinish extends AbstractBotCommand {

    public SendManyFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        new SendManyStage4WriteNotificationNameFinishAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you like to send notifications to all matching contacts?",
                Callbacks.SEND_MANY_FINISH,
                Callbacks.CANCEL);
    }
}
