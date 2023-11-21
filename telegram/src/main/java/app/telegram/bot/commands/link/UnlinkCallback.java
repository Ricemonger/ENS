package app.telegram.bot.commands.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.exceptions.UnlinkException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnlinkCallback extends AbstractBotCommand {

    public UnlinkCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        try {
            sendAnswer("Removing your account linking...");
            botService.unlink(chatId);
        } catch (UnlinkException e) {
            sendAnswer("Error occurred during unlinking exception");
        }
    }
}
