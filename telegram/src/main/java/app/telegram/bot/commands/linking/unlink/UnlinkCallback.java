package app.telegram.bot.commands.linking.unlink;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnlinkCallback extends AbstractBotCommand {

    public UnlinkCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.unlink(chatId);

        sendAnswer("Your account to ENS linking was removed");
    }
}
