package app.telegram.bot.commands.sendall;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendAllDirect extends AbstractBotCommand {

    public SendAllDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            if (isUserActionConfirmFlag()) {
                String answer = "Do you really wand to send notification to all your emergency contacts?";
                askYesOrNoFromInlineKeyboard(answer, Callbacks.SEND_ALL, Callbacks.CANCEL);
            } else {
                new SendAllCallback(bot, update, botService).execute();
            }
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
