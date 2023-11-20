package app.telegram.bot.commands.directCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendAllBotCommand extends AbstractBotCommand {

    public SendAllBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            String answer = "Do you really wand to send notification to all your emergency contacts?";
            sendAnswer(answer);
            askYesOrNoFromInlineKeyboard("SEND_ALL_YES", "SEND_ALL_NO");
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
