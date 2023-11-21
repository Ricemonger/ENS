package app.telegram.bot.commands.sendall;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.SendingException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendAllCallback extends AbstractBotCommand {

    public SendAllCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            botService.sendAll(chatId);
            String answer = "All notifications were successfully sent";
            sendAnswer(answer);
        } catch (SendingException e) {
            String answer = "Error during sending occurred";
            sendAnswer(answer);
        }
    }
}
