package app.telegram.bot.commands.send.sendMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendManyFinishCallback extends AbstractBotCommand {

    public SendManyFinishCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        botService.sendMany(chatId);
        sendText("Messages were sent to matching contacts.");
    }
}
