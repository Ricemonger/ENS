package app.telegram.mytgbot.commands.callbackCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ClearYesBotCommand extends AbstractBotCommand {

    public ClearYesBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = "All  Contacts and Notifications were cleared";
        botService.clear(chatId);
        sendAnswer(answer);
    }
}
