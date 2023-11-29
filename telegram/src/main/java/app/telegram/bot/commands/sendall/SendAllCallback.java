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
    public void executeCommand() {
        try {
            botService.sendAll(chatId);
            sendAnswer("All notifications were successfully sent");
        } catch (SendingException e) {
            sendAnswer("Error during sending occurred");
        }
    }
}
