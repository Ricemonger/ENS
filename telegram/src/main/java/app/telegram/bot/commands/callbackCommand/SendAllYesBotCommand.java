package app.telegram.bot.commands.callbackCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendAllYesBotCommand extends AbstractBotCommand {

    public SendAllYesBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        botService.sendAll(chatId);
        String answer = "All notifications were successfully send";
        sendAnswer(answer);
        //TODO SEND ALL NO SUCCESS MESSAGE
    }
}
